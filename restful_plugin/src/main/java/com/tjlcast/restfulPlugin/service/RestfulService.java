package com.tjlcast.restfulPlugin.service;

import com.codahale.metrics.Timer;
import com.tjlcast.basePlugin.service.DefaultService;
import com.tjlcast.restfulPlugin.data.RequestMsg;
import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class RestfulService extends DefaultService {

    @Override
    public Object service(Object[] data) {
        return null;
    }

    @Resource
    private Timer responses;

    public String sendHTTPRequest(RequestMsg msg) throws IOException {
        final Timer.Context context = responses.time();
        try {
            String response = null;

            switch (msg.getMethod()) {
                case "GET":
                    response = sendGETRequest(msg);
                    break;
                case "POST":
                    response = sendPOSTRequest(msg);
                    break;
                case "DELETE":
                    response = sendDELETERequest(msg);
                    break;
            }

            return  response;
        }finally{
            context.stop();
        }
    }


    public String sendPOSTRequest(RequestMsg msg) throws IOException {

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , msg.getBody());

        Request request = new Request.Builder()
                .url(msg.getUrl())
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }

    }

    public String sendGETRequest(RequestMsg msg) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(msg.getUrl())
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }

    }

    public String sendDELETERequest(RequestMsg msg) throws IOException {

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , msg.getBody());

        Request request = new Request.Builder()
                .url(msg.getUrl())
                .delete(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }

    }

}
