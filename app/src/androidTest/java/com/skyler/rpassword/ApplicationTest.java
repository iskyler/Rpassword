package com.skyler.rpassword;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skyler.rpassword.models.Card;
import com.skyler.rpassword.utils.Secure;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @SmallTest
    public void testAES() {
        byte[] salt = Secure.getSalt();
        try {
            String key = Secure.createHash("hello", salt);
            String content = Secure.AESEncrypt("haha", key.split(":")[2]);
            String content2 = Secure.AESDecrypt(content, key.split(":")[2]);
            assert "haha" == content2;
        } catch (Exception e) {
            Log.d("test", e.toString());
        }
    }

    @SmallTest
    public void testSQL() {
        Card card = new Card("baidu", "a@a.com", "hello", "haha");
        card.save();
        card = new Select()
                .from(Card.class)
                .where("account_name = ?", "baidu")
                .executeSingle();
        Log.d("test", "?");
        assert card.account_password == "hello";
    }

    @LargeTest
    public void testGet() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://rpassword.sinaapp.com", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                assert i == 200;
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

    }

    @LargeTest
    public void testPost() throws UnsupportedEncodingException {
        Logger logger = new Logger();
        logger.username = "skyler";
        logger.password = "haha";
        logger.email = "a@s.com";
        logger.admin = "adfasd";

        AsyncHttpClient client = new AsyncHttpClient();
        ByteArrayEntity entity = new ByteArrayEntity(new Gson().toJson(logger).getBytes("UTF-8"));
        client.post(getContext(), "https://rpassword.sinaapp.com", entity, "application/json",
                new JsonHttpResponseHandler("UTF-8") {
                    @Override
                    public void onSuccess(int status, Header[] headers, JSONObject json) {
                        for (Header item : headers) {
                            System.out.println(item);
                        }
                        assert status == 200;
                    }

                    @Override
                    public void onFailure(int status, Header[] headers, Throwable throwable, JSONObject json) {
                        Log.d("test", "ok5");
                    }
                });
    }

    public class Logger {
        String username;
        String password;
        String email;
        String admin;
    }
}