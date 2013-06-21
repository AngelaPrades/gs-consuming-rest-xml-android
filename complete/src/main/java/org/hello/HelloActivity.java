/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hello;

import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onStart() {
        super.onStart();
        final String url = "http://10.0.2.2:8080/hello-world";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        Greeting greeting = restTemplate.getForObject(url, Greeting.class);

        TextView textView = (TextView) this.findViewById(R.id.text_view);
        textView.setText(greeting.getContent());
    }

}
