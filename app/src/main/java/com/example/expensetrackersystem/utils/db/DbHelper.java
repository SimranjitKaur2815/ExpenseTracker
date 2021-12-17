package com.example.expensetrackersystem.utils.db;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.expensetrackersystem.database.DatabaseClient;
import com.example.expensetrackersystem.database.entities.CurrentUser;
import com.example.expensetrackersystem.database.entities.ExpenseItems;
import com.example.expensetrackersystem.database.entities.User;
import com.example.expensetrackersystem.models.ExpenseDetailModel;
import com.example.expensetrackersystem.models.ExpensesModel;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DbHelper {
    private static final String TAG = "DbHelper";
    private final Executor executor = Executors.newSingleThreadExecutor();
    private static DbHelper instance = null;

    private DbHelper() {
    }

    //To provide singleton DbHelper class instance
    public static synchronized DbHelper getInstance() {
        if (instance == null) {
            instance = new DbHelper();
        }
        return instance;
    }

    /*----- PRIVATE FUNCTIONS-------*/

    //Get current logged in user
    private void CURRENT_USER(Context context, UserDbListener.onGetCurrentUserListener listener) {
        //Executor is used to run the database queries in background.
        executor.execute(() -> {
            //Getting currently logged in user's user_id
            int userId = DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().getCurrentUser();
            if (userId > 0) {
                //Getting user's detail if any user is logged in.
                User user = DatabaseClient.getInstance(context).getAppDatabase().userDao().getUserById(userId);
                if (user != null) {
                    //Running on UI thread as UI element cannot run on background thread.
                    //Callback onSuccess if user is available.
                    ((Activity) context).runOnUiThread(() -> listener.onSuccess(user));
                } else {
                    //Deleting current user if user id not registered.
                    DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().deleteCurrentUser();
                    ((Activity) context).runOnUiThread(() -> listener.onFailure("No current user"));
                }
            } else {
                //Running on UI thread as UI element cannot run on background thread.
                //Returning error msg if no user is currently logged in.
                ((Activity) context).runOnUiThread(() -> listener.onFailure("No current user"));
            }
        });
    }
    /*----- PRIVATE FUNCTIONS-------*/


    /* --------- USER -----------*/

    //Authenticate password method to check whether the entered password is correct or not
    public void authenticatePassword(Context context, User user, String password, UserDbListener.onAuthListener listener) {
        //Executor is used to run the database queries in background.
        executor.execute(() -> {
            //Using try catch as password encryption can throw a GeneralSecurityException
            try {
                //Encrypting password
                String encPassword = AESCrypt.encrypt(password, password);
                if (user.getPassword().equals(encPassword)) {
                    //Running on UI thread as UI element cannot run on background thread.
                    //Callback onSuccess if user entered right password.
                    ((Activity) context).runOnUiThread(listener::onSuccess);
                } else {
                    //Returning error msg if user entered wrong password.
                    ((Activity) context).runOnUiThread(() -> listener.onFailure("Invalid Password"));
                }

            } catch (GeneralSecurityException e) {
                //Running on UI thread as UI element cannot run on background thread.
                //Returning error msg if any error occurs while encrypting.
                ((Activity) context).runOnUiThread(() -> listener.onFailure(e.getLocalizedMessage()));
            }
        });
    }

    //Method to perform the registration task for a new user
    public void registerUser(Context context, User user, UserDbListener.onAuthListener listener) {
        //Executor is used to run the database queries in background.
        executor.execute(() -> {
            User getUser = DatabaseClient.getInstance(context).getAppDatabase().userDao().getUserByName(user.getFirstName(), user.getLastName());
            //Checking whether this name is already registered or not.
            if (getUser == null) {
                //Registering user in the database by inserting user's detail
                DatabaseClient.getInstance(context).getAppDatabase().userDao().insertUser(user);
                //Running on UI thread as UI element cannot run on background thread.
                //Callback onSuccess if user gets registered.
                ((Activity) context).runOnUiThread(listener::onSuccess);
            } else {
                //Callback onFailure if user already registered.
                ((Activity) context).runOnUiThread(() -> listener.onFailure("User already exist"));
            }
        });
    }

    //Method to let use logged in.
    public void loginUser(Context context, int userId, String password, UserDbListener.onAuthListener listener) {
        //Executor is used to run the database queries in background.
        executor.execute(() -> {
            //Using try catch as password encryption can throw a GeneralSecurityException
            try {
                // Password encryption
                String encPassword = AESCrypt.encrypt(password, password);

                // Authenticating password for the given user id.
                User getUser = DatabaseClient.getInstance(context).getAppDatabase().userDao().getUserByIdAndPassword(userId, encPassword);
                if (getUser != null) {
                    // It will delete the currently logged in user's data if exists any.
                    logoutUser(true, context, new UserDbListener.onAuthListener() {
                        @Override
                        public void onSuccess() {
                            // Preparing CurrentUser model
                            CurrentUser currentUser = new CurrentUser(userId);
                            // After deleting we're inserting the newly logged user details into current user entity/table.
                            DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().addCurrentUser(currentUser);
                            //Running on UI thread as UI element cannot run on background thread.
                            //Callback onSuccess if user gets logged in.
                            ((Activity) context).runOnUiThread(listener::onSuccess);

                        }

                        @Override
                        public void onFailure(String msg) {
                            //Callback onFailure and returns failure message if something went wrong while deleting the currently logged in user.
                            ((Activity) context).runOnUiThread(() -> listener.onFailure(msg));

                        }
                    });

                } else {
                    //Callback onFailure as entered password is wrong.
                    ((Activity) context).runOnUiThread(() -> listener.onFailure("Wrong Password"));
                }
            } catch (GeneralSecurityException e) {
                //Callback onFailure and return exception message.
                ((Activity) context).runOnUiThread(() -> listener.onFailure(e.getLocalizedMessage()));
            }
        });
    }

    //Method to delete the currently logged in user's data to perform the Logout task.
    public void logoutUser(boolean callingFromHelper, Context context, UserDbListener.onAuthListener listener) {
        //Executor is used to run the database queries in background.
        executor.execute(() -> {
            try {
                //Deleting current user
                DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().deleteCurrentUser();
                //Checking whether it's called from DbHelper class itself or from anu another class
                if (callingFromHelper)
                    listener.onSuccess();
                else
                    //Running on UI thread as UI element cannot run on background thread.
                    //Callback onSuccess after a successful logout.
                    ((Activity) context).runOnUiThread(listener::onSuccess);
            } catch (Exception e) {
                if (callingFromHelper)
                    listener.onFailure(e.getLocalizedMessage());
                else
                    //Callback onFailure and return exception message.
                    ((Activity) context).runOnUiThread(() -> listener.onFailure(e.getLocalizedMessage()));
            }

        });
    }

    // Method to get all the registered users.
    public void getAllUsers(Context context, UserDbListener.onGetUsersListener listener) {
        //Executor is used to run the database queries in background.
        executor.execute(() -> {
            //Getting list of registered users
            List<User> users = DatabaseClient.getInstance(context).getAppDatabase().userDao().getAllUsers();
            //Running on UI thread as UI element cannot run on background thread.
            ((Activity) context).runOnUiThread(() -> {
                if (users.size() > 0) {
                    //Callback onSuccess and return list of registered users
                    listener.onSuccess(users);
                } else {
                    //Callback onFailure if no users are there.
                    listener.onFailure("No user found");
                }
            });
        });
    }

    // Check if any user is currently logged in
    public void isLoggedIn(Context context, UserDbListener.onAuthListener listener) {
        //Executor is used to run the database queries in background.
        executor.execute(() -> {
            //Getting currently logged in user id if exists any
            int userId = DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().getCurrentUser();
            if (userId > 0) {
                //Getting currently logged in user's data.
                User user = DatabaseClient.getInstance(context).getAppDatabase().userDao().getUserById(userId);
                if (user != null) {
                    //Running on UI thread as UI element cannot run on background thread.
                    //Callback onSuccess if user logged in.
                    ((Activity) context).runOnUiThread(listener::onSuccess);
                } else {
                    //Just for surety, clearing out current user's data
                    DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().deleteCurrentUser();
                    //Callback onFailure if no user logged in
                    ((Activity) context).runOnUiThread(() -> listener.onFailure("No user logged in"));
                }
            } else {
                //Callback onFailure if no user logged in
                ((Activity) context).runOnUiThread(() -> listener.onFailure("No user logged in"));
            }
        });
    }

    //Getting current user's detail
    public void getCurrentUser(Context context, UserDbListener.onGetCurrentUserListener listener) {
        //Executor is used to run the database queries in background.
        executor.execute(() -> {
            //Getting currently logged in user's id.
            int userId = DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().getCurrentUser();
            if (userId > 0) {
                //Get user's all detail
                User user = DatabaseClient.getInstance(context).getAppDatabase().userDao().getUserById(userId);
                if (user != null) {
                    //Running on UI thread as UI element cannot run on background thread.
                    //Callback onSuccess if user found.
                    ((Activity) context).runOnUiThread(() -> listener.onSuccess(user));
                } else {
                    //Just for surety, clearing out current user's data , if user == null
                    DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().deleteCurrentUser();
                    //Callback onFailure if no user found
                    ((Activity) context).runOnUiThread(() -> listener.onFailure("No current user"));
                }
            } else {
                //Callback onFailure if no user found
                ((Activity) context).runOnUiThread(() -> listener.onFailure("No current user"));
            }
        });
    }

    //Delete any user's account
    public void deleteUser(Context context, User deleteUser, UserDbListener.onDeleteAccountListener listener) {
        //Getting currently logged in user's detail
        CURRENT_USER(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                //Checking whether deleting the same logged in user or another.
                if (user.getId() == deleteUser.getId()) {
                    //First have to logout the user.
                    logoutUser(true, context, new UserDbListener.onAuthListener() {
                        @Override
                        public void onSuccess() {
                            //Deleting requested user
                            DatabaseClient.getInstance(context).getAppDatabase().userDao().deleteUser(deleteUser);
                            //Running on UI thread as UI element cannot run on background thread.
                            //Callback onSuccess if user get deleted successfully.
                            ((Activity) context).runOnUiThread(listener::onSuccess);
                        }

                        @Override
                        public void onFailure(String msg) {
                            //Callback onFailure if any error.
                            ((Activity) context).runOnUiThread(() -> listener.onFailure(msg));
                        }
                    });
                } else {
                    //If another user, requesting to enter password for authenticity
                    listener.onPasswordRequired(() -> {
                        //Executor is used to run the database queries in background.
                        executor.execute(() -> {
                            //Deleting requested user
                            DatabaseClient.getInstance(context).getAppDatabase().userDao().deleteUser(deleteUser);
                        });
                        //Callback onSuccess if user get deleted successfully.
                        listener.onSuccess();
                    });
                }
            }

            @Override
            public void onFailure(String msg) {
                //Callback onFailure if any error.
                listener.onFailure(msg);
            }
        });

    }

    //Update the user's first and last name.
    public void updateUsername(Context context, String newFirstname, String newLastname, String password, UserDbListener.onAuthListener listener) {
        //Getting current user
        CURRENT_USER(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                //For password encryption, try catch needed
                try {
                    //Encrypting password
                    String encryptPassword = AESCrypt.encrypt(password, password);
                    //Authenticating password to change the name
                    if (user.getPassword().equals(encryptPassword)) {
                        //Proceed updating username
                        user.setFirstName(newFirstname);
                        user.setLastName(newLastname);
                        executor.execute(() -> {
                            //Updating request username.
                            DatabaseClient.getInstance(context).getAppDatabase().userDao().updateUser(user);
                        });
                        //Callback onSuccess username gets changed.
                        listener.onSuccess();

                    } else {
                        //Callback onFailure if password is invalid.
                        listener.onFailure("Invalid password");
                    }
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                    //Callback onFailure if any error.
                    listener.onFailure(e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(String msg) {
                //Callback onFailure if any error.
                listener.onFailure(msg);
            }
        });
    }


    //to  update the user's password
    public void updatePassword(Context context, String oldPassword, String newPassword, UserDbListener.onAuthListener listener) {
        //Getting currently logged in user's detail
        CURRENT_USER(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                //For password encryption, try catch needed
                try {
                    //Encrypting password
                    String encryptPassword = AESCrypt.encrypt(oldPassword, oldPassword);
                    String encryptNewPassword = AESCrypt.encrypt(newPassword, newPassword);

                    //Authenticating password with old one
                    if (user.getPassword().equals(encryptPassword)) {
                        //Proceed updating password
                        user.setPassword(encryptNewPassword);
                        //Executor is used to run the database queries in background.
                        executor.execute(() -> {
                            //Updating requested password
                            DatabaseClient.getInstance(context).getAppDatabase().userDao().updateUser(user);
                        });
                        //Callback onSuccess username gets changed.
                        listener.onSuccess();

                    } else {
                        //Callback onFailure if password is invalid.
                        listener.onFailure("Invalid old password");
                    }
                } catch (GeneralSecurityException e) {
                    //Callback onFailure if any error.
                    e.printStackTrace();
                    listener.onFailure(e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(String msg) {
                //Callback onFailure if any error.
                listener.onFailure(msg);
            }
        });
    }


    /* --------- USER -----------*/



    /* --------- EXPENSES -----------*/

    //Get all the expenses
    public void getExpenses(Context context, int userId, String expenseDate, ExpenseDbListener.onGetExpensesListener listener) {
        //Executor is used to run the database queries in background.
        executor.execute(() -> {
            List<ExpenseItems> items = DatabaseClient.getInstance(context).getAppDatabase().expensesDao().getExpenses(userId, expenseDate);
            for (ExpenseItems item : items) {
                Log.e(TAG, "getExpenses: " + item.toString());
            }
            if (items.size() > 0) {
                ((Activity) context).runOnUiThread(() -> listener.onSuccess(items));
            } else {
                ((Activity) context).runOnUiThread(() -> listener.onFailure("No expenses yet."));
            }
        });

    }

    public void addExpense(Context context, ExpenseItems expenseItems, ExpenseDbListener.onAddExpenseListener listener) {
        CURRENT_USER(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                //Executor is used to run the database queries in background.
                executor.execute(() -> {
                    DatabaseClient.getInstance(context).getAppDatabase().expensesDao().insertExpense(expenseItems);
                    ((Activity) context).runOnUiThread(listener::onSuccess);
                });
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    public void updateExpense(Context context, ExpenseItems expenseItem, ExpenseDbListener.onAddExpenseListener listener) {
        CURRENT_USER(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                //Executor is used to run the database queries in background.
                executor.execute(() -> {
                    DatabaseClient.getInstance(context).getAppDatabase().expensesDao()
                            .updateExpense(expenseItem);
                    ((Activity) context).runOnUiThread(listener::onSuccess);
                });
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    public void deleteExpense(Context context, ExpenseItems expenseItem, ExpenseDbListener.onDeleteExpenseListener listener) {
        CURRENT_USER(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                //Executor is used to run the database queries in background.
                executor.execute(() -> {
                    DatabaseClient.getInstance(context).getAppDatabase().expensesDao().delete(expenseItem);
                    ((Activity) context).runOnUiThread(listener::onSuccess);
                });
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    public void getAllExpenses(Context context, ExpenseDbListener.onGetAllExpenseListener listener) {
        CURRENT_USER(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                //Executor is used to run the database queries in background.
                executor.execute(() -> {
                    List<String> dates = DatabaseClient.getInstance(context).getAppDatabase().expensesDao().getExpensesDates();
                    if (dates.size() > 0) {
                        List<ExpensesModel> expensesModelList = new ArrayList<>();
                        for (String date : dates) {
                            ExpensesModel expensesModel = new ExpensesModel();
                            List<ExpenseItems> expenseItems = DatabaseClient.getInstance(context).getAppDatabase().expensesDao().getExpenses(user.getId(), date);
                            if (expenseItems.size() > 0) {
                                Double totPrice = 0.0d;
                                for (ExpenseItems item : expenseItems) {
                                    totPrice += item.getItemPrice();
                                }
                                expensesModel.setTotalPrice(totPrice);
                                expensesModel.setSubmittedDate(date);
                                expensesModel.setExpenseItems(expenseItems);
                                expensesModelList.add(expensesModel);
                            }

                        }
                        listener.onSuccess(expensesModelList);
                    } else {
                        listener.onFailure("No data");
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    public void deleteExpenseByDate(Context context, String createdDate, ExpenseDbListener.onDeleteExpenseListener listener) {
        CURRENT_USER(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                //Executor is used to run the database queries in background.
                executor.execute(() -> {
                    DatabaseClient.getInstance(context).getAppDatabase().expensesDao().deleteByDate(createdDate);
                    ((Activity) context).runOnUiThread(listener::onSuccess);
                });
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    public void getExpenseDetails(Context context, ExpenseDbListener.onGetExpenseDetailsListener listener) {
        CURRENT_USER(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                //Executor is used to run the database queries in background.
                executor.execute(() -> {
                    ExpenseDetailModel expenseDetailModel = DatabaseClient.getInstance(context).getAppDatabase().expensesDao().getExpensesDetails(user.getId());
                    ((Activity) context).runOnUiThread(() -> listener.onSuccess(expenseDetailModel));

                });
            }

            @Override
            public void onFailure(String msg) {
                ((Activity) context).runOnUiThread(() -> listener.onFailure(msg));

            }
        });
    }
    /* --------- EXPENSES -----------*/

}
