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

    public static synchronized DbHelper getInstance() {
        if (instance == null) {
            instance = new DbHelper();
        }
        return instance;
    }
    /*----- PRIVATE FUNCTIONS-------*/

    private void CURRENT_USER(Context context, UserDbListener.onGetCurrentUserListener listener) {
        executor.execute(() -> {
            int userId = DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().getCurrentUser();
            if (userId > 0) {
                User user = DatabaseClient.getInstance(context).getAppDatabase().userDao().getUserById(userId);
                if (user != null) {
                    ((Activity) context).runOnUiThread(() -> listener.onSuccess(user));
                } else {
                    DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().deleteCurrentUser();
                    ((Activity) context).runOnUiThread(() -> listener.onFailure("No current user"));
                }
            } else {
                ((Activity) context).runOnUiThread(() -> listener.onFailure("No current user"));
            }
        });
    }
    /*----- PRIVATE FUNCTIONS-------*/


    /* --------- USER -----------*/

    public void authenticatePassword(Context context, User user, String password, UserDbListener.onAuthListener listener) {
        executor.execute(() -> {
            try {
                String encPassword = AESCrypt.encrypt(password, password);
                if (user.getPassword().equals(encPassword)) {
                    ((Activity) context).runOnUiThread(listener::onSuccess);
                } else {
                    ((Activity) context).runOnUiThread(() -> listener.onFailure("Invalid Password"));
                }

            } catch (GeneralSecurityException e) {
                ((Activity) context).runOnUiThread(() -> listener.onFailure(e.getLocalizedMessage()));

            }
        });
    }

    public void registerUser(Context context, User user, UserDbListener.onAuthListener listener) {
        executor.execute(() -> {
            User getUser = DatabaseClient.getInstance(context).getAppDatabase().userDao().getUserByName(user.getFirstName(), user.getLastName());
            if (getUser == null) {
                DatabaseClient.getInstance(context).getAppDatabase().userDao().insertUser(user);
                ((Activity) context).runOnUiThread(listener::onSuccess);
            } else {
                ((Activity) context).runOnUiThread(() -> listener.onFailure("User already exist"));
            }
        });
    }

    public void loginUser(Context context, int userId, String password, UserDbListener.onAuthListener listener) {
        executor.execute(() -> {
            try {
                String encPassword = AESCrypt.encrypt(password, password);
                User getUser = DatabaseClient.getInstance(context).getAppDatabase().userDao().getUserByIdAndPassword(userId, encPassword);
                if (getUser != null) {
                    logoutUser(true, context, new UserDbListener.onAuthListener() {
                        @Override
                        public void onSuccess() {
                            CurrentUser currentUser = new CurrentUser(userId);
                            DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().addCurrentUser(currentUser);
                            ((Activity) context).runOnUiThread(listener::onSuccess);

                        }

                        @Override
                        public void onFailure(String msg) {
                            ((Activity) context).runOnUiThread(() -> listener.onFailure(msg));

                        }
                    });

                } else {
                    ((Activity) context).runOnUiThread(() -> listener.onFailure("Wrong Password"));
                }
            } catch (GeneralSecurityException e) {
                ((Activity) context).runOnUiThread(() -> listener.onFailure(e.getLocalizedMessage()));
            }
        });
    }

    public void logoutUser(boolean callingFromHelper, Context context, UserDbListener.onAuthListener listener) {
        executor.execute(() -> {
            try {
                DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().deleteCurrentUser();
                if (callingFromHelper)
                    listener.onSuccess();
                else
                    ((Activity) context).runOnUiThread(listener::onSuccess);
            } catch (Exception e) {
                if (callingFromHelper)
                    listener.onFailure(e.getLocalizedMessage());
                else
                    ((Activity) context).runOnUiThread(() -> listener.onFailure(e.getLocalizedMessage()));
            }

        });
    }

    public void getAllUsers(Context context, UserDbListener.onGetUsersListener listener) {
        executor.execute(() -> {
            List<User> users = DatabaseClient.getInstance(context).getAppDatabase().userDao().getAllUsers();
            ((Activity) context).runOnUiThread(() -> {
                if (users.size() > 0) {
                    listener.onSuccess(users);
                } else {
                    listener.onFailure("No user found");
                }
            });
        });
    }

    public void isLoggedIn(Context context, UserDbListener.onAuthListener listener) {
        executor.execute(() -> {
            int userId = DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().getCurrentUser();
            if (userId > 0) {
                User user = DatabaseClient.getInstance(context).getAppDatabase().userDao().getUserById(userId);
                if (user != null) {
                    ((Activity) context).runOnUiThread(listener::onSuccess);
                } else {
                    DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().deleteCurrentUser();
                    ((Activity) context).runOnUiThread(() -> listener.onFailure("No current user"));
                }
            } else {
                ((Activity) context).runOnUiThread(() -> listener.onFailure("No current user"));
            }
        });
    }

    public void getCurrentUser(Context context, UserDbListener.onGetCurrentUserListener listener) {
        executor.execute(() -> {
            int userId = DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().getCurrentUser();
            if (userId > 0) {
                User user = DatabaseClient.getInstance(context).getAppDatabase().userDao().getUserById(userId);
                if (user != null) {
                    ((Activity) context).runOnUiThread(() -> listener.onSuccess(user));
                } else {
                    DatabaseClient.getInstance(context).getAppDatabase().currentUserDao().deleteCurrentUser();
                    ((Activity) context).runOnUiThread(() -> listener.onFailure("No current user"));
                }
            } else {
                ((Activity) context).runOnUiThread(() -> listener.onFailure("No current user"));
            }
        });
    }

    public void deleteUser(Context context, User deleteUser, UserDbListener.onDeleteAccountListener listener) {
        CURRENT_USER(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                if (user.getId() == deleteUser.getId()) {
                    logoutUser(true, context, new UserDbListener.onAuthListener() {
                        @Override
                        public void onSuccess() {
                            DatabaseClient.getInstance(context).getAppDatabase().userDao().deleteUser(deleteUser);
                            ((Activity) context).runOnUiThread(listener::onSuccess);
                        }

                        @Override
                        public void onFailure(String msg) {
                            ((Activity) context).runOnUiThread(() -> listener.onFailure(msg));
                        }
                    });
                } else {
                    listener.onPasswordRequired(() -> {
                        executor.execute(() -> {
                            DatabaseClient.getInstance(context).getAppDatabase().userDao().deleteUser(deleteUser);
                        });
                        listener.onSuccess();
                    });
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });

    }

    public void updateUsername(Context context, String newFirstname,String newLastname, String password, UserDbListener.onAuthListener listener) {
        CURRENT_USER(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                try {
                    String encryptPassword=AESCrypt.encrypt(password,password);
                    if(user.getPassword().equals(encryptPassword)){
                        //Proceed updating username
                        user.setFirstName(newFirstname);
                        user.setLastName(newLastname);
                        executor.execute(()->{
                            DatabaseClient.getInstance(context).getAppDatabase().userDao().updateUser(user);
                        });
                        listener.onSuccess();

                    }
                    else
                    {
                        listener.onFailure("Invalid password");
                    }
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    public void updatePassword(Context context, String oldPassword, String newPassword, UserDbListener.onAuthListener listener){
        CURRENT_USER(context, new UserDbListener.onGetCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                try {
                    String encryptPassword=AESCrypt.encrypt(oldPassword,oldPassword);
                    String encryptNewPassword=AESCrypt.encrypt(newPassword,newPassword);

                    if(user.getPassword().equals(encryptPassword)){
                        //Proceed updating username
                        user.setPassword(encryptNewPassword);
                        executor.execute(()->{
                            DatabaseClient.getInstance(context).getAppDatabase().userDao().updateUser(user);
                        });
                        listener.onSuccess();

                    }
                    else
                    {
                        listener.onFailure("Invalid old password");
                    }
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }


    /* --------- USER -----------*/



    /* --------- EXPENSES -----------*/

    public void getExpenses(Context context, int userId, String expenseDate, ExpenseDbListener.onGetExpensesListener listener) {
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
                executor.execute(() -> {
                    Log.e(TAG, "UpdateExpense: " + expenseItem.toString());
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
                executor.execute(() -> {
                    List<String> dates = DatabaseClient.getInstance(context).getAppDatabase().expensesDao().getExpensesDates();
                    if (dates.size() > 0) {
                        List<ExpensesModel> expensesModelList = new ArrayList<>();
                        for (String date : dates) {
                            ExpensesModel expensesModel = new ExpensesModel();
                            List<ExpenseItems> expenseItems = DatabaseClient.getInstance(context).getAppDatabase().expensesDao().getExpenses(user.getId(), date);
                            Double totPrice = 0.0d;
                            for (ExpenseItems item : expenseItems) {
                                totPrice += item.getItemPrice();
                            }
                            expensesModel.setTotalPrice(totPrice);
                            expensesModel.setSubmittedDate(date);
                            expensesModel.setExpenseItems(expenseItems);
                            expensesModelList.add(expensesModel);

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
