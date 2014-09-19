package com.skyler.rpassword.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.skyler.rpassword.R;
import com.skyler.rpassword.models.Card;
import com.skyler.rpassword.models.User;
import com.skyler.rpassword.utils.Secure;

import java.security.Security;
import java.util.Scanner;

public class MainActivity extends Activity {
    private SwipeListView card_list;
    private Button card_add;
    private CardAdapter card_adapter;
    private User user;
    private String root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = new Select()
                .from(User.class)
                .where("username = ?", LoginActivity.getUsername_login())
                .executeSingle();

        if (user == null) {
            Log.d("test", "No user found");
        }

        card_list = (SwipeListView) findViewById(R.id.main_card);
        card_adapter = new CardAdapter(
                MainActivity.this,
                R.layout.card,
                null,
                new String[]{"Card.account_name"},
                new int[]{R.id.main_card_title},
                0);
        card_list.setAdapter(card_adapter);
        MainActivity.this.getLoaderManager().initLoader(0, null, new LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int arg0, Bundle cursor) {
                return new CursorLoader(MainActivity.this,
                        ContentProvider.createUri(Card.class, null),
                        null, null, null, null
                );
            }

            @Override
            public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
                ((SimpleCursorAdapter) card_list.getAdapter()).swapCursor(cursor);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> arg0) {
                ((SimpleCursorAdapter) card_list.getAdapter()).swapCursor(null);

            }
        });

        card_add = (Button) findViewById(R.id.main_card_add);
        card_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (root == null) {
                    new CustomDialog().root_auth();
                } else {
                    new CustomDialog().card_add();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_backup:
                break;
            case R.id.action_restore:
                break;
            case R.id.action_about:
                break;
            case R.id.action_logout:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class CardAdapter extends SimpleCursorAdapter {
        private Context context;
        private int layout;
        private Cursor c;

        public CardAdapter(Context context, int layout, Cursor c,
                           String[] from, int[] to, int flag) {
            super(context, layout, c, from, to, flag);
            this.context = context;
            this.layout = layout;
            this.c = c;
        }

        @Override
        public void bindView(View view, Context context, Cursor c) {
            View convertView = null;
            if (view == null) {
                convertView = getLayoutInflater().inflate(this.layout, null);
            } else {
                convertView = view;
            }
            TextView title = (TextView) convertView.findViewById(R.id.main_card_title);
            Button card_edit = (Button) convertView.findViewById(R.id.card_edit);
            Button card_query = (Button) convertView.findViewById(R.id.card_query);
            Button card_delete = (Button) convertView.findViewById(R.id.card_delete);

            final String sAccount = c.getString(c.getColumnIndex("Card.account_name"));
            title.setText(sAccount);

            card_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (root == null) {
                        new CustomDialog().root_auth();
                    }
                    else {
                        new CustomDialog().card_edit(sAccount);
                    }
                }
            });

            card_query.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (root == null) {
                        new CustomDialog().root_auth();
                    }
                    else {
                        new CustomDialog().card_query(sAccount);
                    }
                }
            });

            card_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (root == null) {
                        new CustomDialog().root_auth();
                    }
                    else {
                        new CustomDialog().card_delete(sAccount);
                    }
                }
            });


        }
    }

    public class CustomDialog {

        public CustomDialog() {
        }

        public void card_add() {
            final View cardAdd = getLayoutInflater().inflate(R.layout.dialog_card_add, null);
            new AlertDialog.Builder(MainActivity.this)
                    .setView(cardAdd)
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText account = (EditText) cardAdd.findViewById(R.id.card_add_account);
                                    EditText account_username = (EditText) cardAdd.findViewById(R.id.card_add_username);
                                    EditText account_password = (EditText) cardAdd.findViewById(R.id.card_add_password);
                                    card_add(account, account_username, account_password);
                                }
                            })
                    .setTitle(R.string.add_account)
                    .show();
        }

        public void card_edit(final String sAccount) {
            final View cardEdit = getLayoutInflater().inflate(R.layout.dialog_card_edit, null);
            TextView account = (TextView) cardEdit.findViewById(R.id.card_edit_account);
            account.setText(sAccount);
            new AlertDialog.Builder(MainActivity.this)
                    .setView(cardEdit)
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText account_password = (EditText) cardEdit.findViewById(R.id.card_edit_new_password);
                                    EditText account_password_again = (EditText) cardEdit.findViewById(R.id.card_edit_new_password_again);

                                    card_edit(sAccount, account_password, account_password_again);
                                }
                            })
                    .setTitle(R.string.button_card_edit)
                    .show();
        }

        public void card_query(final String sAccount) {
            final View cardQuery = getLayoutInflater().inflate(R.layout.dialog_card_query, null);
            TextView account = (TextView) cardQuery.findViewById(R.id.card_query_account);
            account.setText(sAccount);
            Card card = new Select()
                    .from(Card.class)
                    .where("account_name = ?", sAccount)
                    .executeSingle();
            TextView username = (TextView) cardQuery.findViewById(R.id.card_quey_username);
            final EditText password = (EditText) cardQuery.findViewById(R.id.card_query_password);
            username.setText(card.account_username);
            try {
                Log.d("test", card.account_password);
                Log.d("test", sAccount);
                String key = Secure.createHash( root, Secure.fromHex(card.salt));
                Log.d("test", key);
                String content = Secure.AESDecrypt(card.account_password,
                        key.split(":")[2]);
                Log.d("test", "password = " + content);
                password.setText(content);
            } catch (Exception e) {
                Log.d("test", e.toString());
            }

            Log.d("test", "ok1");
            new AlertDialog.Builder(MainActivity.this)
                    .setView(cardQuery)
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                    .setPositiveButton(R.string.clip,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ClipboardManager cbm = (ClipboardManager) getSystemService(
                                            CLIPBOARD_SERVICE
                                    );
                                    cbm.setPrimaryClip(ClipData.newPlainText("password",
                                            password.getText().toString()));
                                }
                            })
                    .setTitle(R.string.card_query)
                    .show();
        }

        public void card_delete(final String sAccount) {
            View cardDelete = getLayoutInflater().inflate(R.layout.dialog_card_delete, null);
            TextView card_delete = (TextView) cardDelete.findViewById(R.id.card_delete);
            String warning = getString(R.string.delete_warning) + sAccount + "?";
            card_delete.setText(warning);
            new AlertDialog.Builder(MainActivity.this)
                    .setView(cardDelete)
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Delete().from(Card.class)
                                            .where("account_name = ?", sAccount)
                                            .execute();
                                    Toast.makeText(MainActivity.this,
                                            getString(R.string.success_delete), Toast.LENGTH_SHORT)
                                            .show();
                                    card_adapter.notifyDataSetChanged();
                                }
                            })
                    .setTitle(R.string.button_card_delete)
                    .show();
        }

        public void root_auth() {
            final View rootAuth = getLayoutInflater().inflate(R.layout.dialog_root_auth, null);
            new AlertDialog.Builder(MainActivity.this)
                    .setView(rootAuth)
                    .setPositiveButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText root_view = (EditText) rootAuth.findViewById(R.id.dialog_root);
                                    root_auth(root_view);
                                }
                            })
                    .setTitle(R.string.title_activity_root_auth)
                    .show();
        }

        public void card_add(EditText account, EditText account_username,
                             EditText account_password) {
            account.setError(null);
            account_username.setError(null);
            account_password.setError(null);

            String sAccount = account.getText().toString();
            String sUsername = account_username.getText().toString();
            String sPassword = account_password.getText().toString();

            View focus = null;
            Boolean cancel = false;

            if (sPassword.isEmpty()) {
                account_password.setError(getString(R.string.error_field_required));
                focus = account_password;
                cancel = true;
            }
            if (sUsername.isEmpty()) {
                account_username.setError(getString(R.string.error_field_required));
                focus = account_username;
                cancel = true;
            }
            if (sAccount.isEmpty()) {
                account.setError(getString(R.string.error_field_required));
                focus = account;
                cancel = true;
            }
            Log.d("test", "cancel = " + Boolean.toString(cancel));
            Log.d("test", "root = " + root);
            if (cancel) {
                focus.requestFocus();
            } else {
                try {
                    byte[] salt = Secure.getSalt();
                    String key = Secure.createHash(root, salt);
                    String origKey = key.split(":")[2];
                    String content = Secure.AESEncrypt(sPassword, origKey);
                    Card card = new Card(sAccount, sUsername, content, key.split(":")[1]);
                    Log.d("test", key);
                    Log.d("test", card.account_password);
                    card.save();
                    card_adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this,
                            getString(R.string.success_add), Toast.LENGTH_SHORT)
                            .show();
                } catch (Exception e) {
                    Log.d("test", e.toString());

                    Toast.makeText(MainActivity.this,
                            getString(R.string.fail_add), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

        public void card_edit(String account, EditText password,
                              EditText password_again) {
            password.setError(null);
            password_again.setError(null);
            String sPassword = password.getText().toString();
            String sAgain = password_again.getText().toString();
            Boolean cancel = false;
            View focus = null;

            if (!sAgain.equals(sPassword)) {
                password_again.setError(getString(R.string.match_error));
                focus = password_again;
                cancel = true;
            }
            if (sAgain.isEmpty()) {
                password_again.setError(getString(R.string.error_field_required));
                focus = password_again;
                cancel = true;
            }
            if (sPassword.isEmpty()) {
                password.setError(getString(R.string.error_field_required));
                focus = password;
                cancel = true;
            }

            if (cancel) {
                focus.requestFocus();
            } else {
                Card card = new Select()
                        .from(Card.class)
                        .where("account_name = ?", account)
                        .executeSingle();
                try {
                    byte[] salt = Secure.getSalt();
                    String key = Secure.createHash(root, salt);
                    String origKey = key.split(":")[2];
                    String content = Secure.AESEncrypt(sPassword, origKey);
                    card.salt = key.split(":")[1];
                    card.account_password = content;
                    card.save();
                    Toast.makeText(MainActivity.this,
                            getString(R.string.success_modify), Toast.LENGTH_SHORT)
                            .show();
                } catch (Exception e) {
                    Log.d("test", e.toString());

                    Toast.makeText(MainActivity.this,
                            getString(R.string.fail_modify), Toast.LENGTH_SHORT)
                            .show();
                }

            }
        }


        public Boolean root_auth(EditText editText) {
            editText.setError(null);

            String root_test = editText.getText().toString();
            if (root_test.isEmpty()) {
                editText.setError(getString(R.string.error_field_required));
                editText.requestFocus();
            }

            try {
                if (Secure.validatePassword(root_test, user.root)) {
                    root = root_test;
                    Toast.makeText(MainActivity.this,
                            getString(R.string.success_auth), Toast.LENGTH_SHORT)
                            .show();
                    return true;
                }
            } catch (Exception e) {
                Log.d("test", e.toString());
            }

            Toast.makeText(MainActivity.this,
                    getString(R.string.fail_auth), Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
    }
}
