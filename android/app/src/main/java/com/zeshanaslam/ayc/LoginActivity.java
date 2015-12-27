package com.zeshanaslam.ayc;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zeshanaslam.ayc.Utils.CallBack;
import com.zeshanaslam.ayc.Utils.HTTPSManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.login_username)
    EditText _usernameText;
    @Bind(R.id.login_password)
    EditText _passwordText;
    @Bind(R.id.login_button)
    Button _loginButton;

    @BindString(R.string.server_url)
    String _serverURL;
    @BindString(R.string.invalid_username)
    String _invalidUsername;
    @BindString(R.string.invalid_password)
    String _invalidPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        if (!validateInput()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        HTTPSManager httpsManager = new HTTPSManager();
        httpsManager.runConnection(_serverURL + "/login?user=" + username + "&pass=" + password, new CallBack() {

            @Override
            public void onRequestComplete(String response) {
                System.out.println(response);
            }

            @Override
            public void onRequestFailed() {
                System.out.println("Failed");
            }
        });
    }

    private boolean loginCheck(String JSON) {
        boolean login = true;
        try {
            JSONObject jsonObject = new JSONObject(JSON);

            if (jsonObject.has("code")) {
                login = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return login;
    }

    private void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    private boolean validateInput() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty()) {
            _usernameText.setError(_invalidUsername);
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError(_invalidPassword);
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
