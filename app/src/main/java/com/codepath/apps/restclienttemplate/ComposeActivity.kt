package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var tvCharCount: EditText

    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        //tvCharCount = findViewById(R.id.tvCharCount)

        client = TwitterApplication.getRestClient(this)


        btnTweet.setOnClickListener {
            //grab content of edit text
            val tweetContent = etCompose.text.toString()

            //1. make sure tweet isn't empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweets not allowed!", Toast.LENGTH_SHORT).show()
            }

            //2. make sure tweet is not too long
            else if (tweetContent.length > 140) {
                Toast.makeText(this, "Tweet is too long! Limit is 140 characters.", Toast.LENGTH_SHORT).show()
            }

            else {
                //make twitter api call to edit tweet
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {

                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        Log.i(TAG, "Successfully published tweet")

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("teet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()

                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet", throwable)
                    }

                })
            }

        }
    }

    companion object {
        val TAG =  "Compose Activity"
    }
}