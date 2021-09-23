package com.pkall.dolly;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import com.example.tfliesample.TextClassificationClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimerTask;
import android.widget.ImageButton;
import android.widget.TextView;
import org.json.JSONObject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.pkall.dolly.Constants.Server_url1;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageButton micbutton;
    private Button sendbutton;
    private EditText userinputtext;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView usaidtext;
    private TextView dollysaid;
    private ProgressBar dollyprogress;
    private Switch topswitch;
    private ImageView micimageview;

    private TextClassificationClient client;
    Boolean state = true;
    Boolean state1 = true;
    //private TextView resultTextView;
    //private EditText inputEditText;
    private Handler handler;
    //private ScrollView scrollView;

    TextToSpeech t1;
    Context context;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    String[] msgs = { "Hi I am Dolly your AI friend"};
    String userstr;
    HashMap data;
    String replystr;
    ImageView imageViewface;
    ImageView dollymouth;
    Boolean dollymute =  false;
    Boolean chatarea = true;



    int z=0;
    ImageView imageview;
    int face[] = {R.drawable.f1, R.drawable.f2, R.drawable.f3, R.drawable.f4, R.drawable.f5, R.drawable.f6 };

    String [] joy = {};
    String [] joyresponses = {};


    //firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    //private FirebaseUser user;
    private FirebaseAnalytics firebaseAnalytics;

    //ArrayList<String> msgs = new ArrayList<String>();
    //mylist.add(mystring);




    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        handler.post(
                () -> {
                    client.load();
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        handler.post(
                () -> {
                    client.unload();
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.v(TAG, "onCreate");

        client = new com.example.tfliesample.TextClassificationClient(getApplicationContext());
        handler = new Handler();


        imageview = findViewById(R.id.imageview1);
        micbutton = findViewById(R.id.micbutton);
        dollymouth =  findViewById(R.id.imageView8);

        JSONObject jsonBody = new JSONObject();
        usaidtext = findViewById(R.id.textView1);
        dollysaid = findViewById(R.id.textView2);
        dollyprogress = findViewById(R.id.progressBar);
        imageViewface =  findViewById(R.id.imageView6);
        topswitch = findViewById(R.id.switch1);
        micimageview = findViewById(R.id.imageView8);
        micimageview.setVisibility(INVISIBLE);

        sendbutton = findViewById(R.id.sendbutton);
        userinputtext = findViewById(R.id.userinputText);
        imageview.setImageResource(face[0]);
        dollyprogress.setVisibility(INVISIBLE);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        dollyface();
        dollylisten();


        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = t1.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //changing the voice type
       // Set<String> a=new HashSet<>();
        //a.add("male");//here you can give male if you want to select male voice.
       // Voice v=new Voice("en-us-x-sfg#male_2-local",new Locale("en","US"),400,200,true,a);
       // t1.setVoice(v);
       // t1.setSpeechRate(0.8f);


        context = getApplicationContext();
//        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout1);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recylerViewLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(recylerViewLayoutManager);
        recyclerViewAdapter = new MyAdapter(context, msgs, false);
        recyclerView.setAdapter(recyclerViewAdapter);

        //recyclerView.setFadingEdgeLength(120);
        //recyclerView.setVerticalFadingEdgeEnabled();

        micbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Toast.makeText(getApplicationContext(),"Hello Dolly",Toast.LENGTH_SHORT).show();
                promptSpeechInput();


                Bundle bundle = new Bundle();
                bundle.putString("micbutton", "presses");
               // bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text");
                firebaseAnalytics.logEvent("micbutton", bundle);

            }
        });

        topswitch.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                            //Log.d(TAG,"top Switch toggled");

                                             chatarea = !chatarea;
                                             Log.d(TAG,chatarea.toString());

                                             if(!chatarea){
                                                micbutton.setVisibility(INVISIBLE);
                                                sendbutton.setVisibility(INVISIBLE);
                                                userinputtext.setVisibility(INVISIBLE);
                                                recyclerView.setVisibility(INVISIBLE);
                                                micimageview.setVisibility(VISIBLE);
                                             }

                                             if(chatarea){
                                                 micbutton.setVisibility(VISIBLE);
                                                 sendbutton.setVisibility(VISIBLE);
                                                 userinputtext.setVisibility(VISIBLE);
                                                 recyclerView.setVisibility(VISIBLE);
                                                 micimageview.setVisibility(INVISIBLE);
                                             }

                                         }
                                     }
        );


        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Toast.makeText(getApplicationContext(),"Hello Dolly",Toast.LENGTH_SHORT).show();
                userstr = userinputtext.getText().toString();
                userinputtext.setText("");
                if(userstr != ""){
                    int currentSize = msgs.length;
                    int newSize = currentSize + 1;
                    String[] tempArray = new String[ newSize ];
                    for (int i=0; i < currentSize; i++)
                    {
                        tempArray[i] = msgs [i];
                    }
                    tempArray[newSize- 1] = userstr;
                    msgs = tempArray;
                    recyclerViewAdapter = new MyAdapter(context, msgs, true);
                    recyclerView.setAdapter(recyclerViewAdapter);

                    postdatatoserver(userstr);

                   // dollyreply(userstr);
                    //REAL MODEL
                    classify(userstr);

                   // facetalk(1);

                }
            }
        });

        // Reading the file in oncreate function
      //  try {
        //    readtxtfilelines("jj", joyresponses);
       // }
       // catch (IOException ex){
         //   Log.e("Responses", "NOT Read");
       // }
        //Log.e("Responses lengh", String(joyresponses.length));
    }

    /**
     * Send input text to TextClassificationClient and get the classify messages.
     */
    private void classify(final String text) {
        handler.post(
                () -> {
                    // Run text classification with TF Lite.
                    List<TextClassificationClient.Result> results = client.classify(text);

                    // Show classification result on screen
                    //showResult(text, results);


                    for (int i = 0; i < results.size(); i++) {
                        TextClassificationClient.Result result = results.get(i);

                        if(result.getConfidence() > 0.5){
                            Log.d("resukts", result.getConfidence().toString());

                            Log.d("resukts", result.getTitle());
                            dollyreply(result.getTitle());
                        }
                        //Log.d("resukts", result.getTitle());
                        //Log.d("resukts", result.getConfidence().toString());
                        //textToShow +=
                          //      String.format("    %s: %s\n", result.getTitle(), result.getConfidence());
                    }
                });
    }


    /**
     * Show classification result on the logs.
     */

    private void showResult(final String inputText, final List<TextClassificationClient.Result> results) {
        // Run on UI thread as we'll updating our app UI
        runOnUiThread(
                () -> {
                    String textToShow = "Input: " + inputText + "\nOutput:\n";
                    for (int i = 0; i < results.size(); i++) {
                        TextClassificationClient.Result result = results.get(i);
                        textToShow +=
                                String.format("    %s: %s\n", result.getTitle(), result.getConfidence());
                    }
                    textToShow += "---------\n";

                    Log.e("OUTPUT from Model", textToShow);

                    // Append the result to the UI.
                    //resultTextView.append(textToShow);

                    // Clear the input text.
                   // inputEditText.getText().clear();

                    // Scroll to the bottom to show latest entry's classification result.
                    //scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                });


    }
/*
    public void readtxtfilelines(String filename, String [] outputlines) throws IOException {

        InputStream inputStream = getResources().openRawResource(R.id.dummy_button);
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        String eachline = bufferedReader.readLine();


        
         while (eachline != null) {
         //    `the words in the file are separated by space`, so to get each words
            String[] responses = eachline.split("\n");
            eachline = bufferedReader.readLine();
            Log.e("Joy response", Arrays.toString(responses));


        }
    }
*/
  /*  public void facetalk(final int talk){
        Thread timer = new Thread() {

            public void run() {
                try {
                    //sleep(2000);
                    //if(talk = 1){
                     //   for (z = 0; z < face.length-1; z++) {
                      //   if (z < face.length) {
                       //     sleep(200);
                       //     runOnUiThread(new Runnable() {
                         //       public void run() {
                        //            imageview.setImageResource(face[z]);
                         //       }
                         //   });
                        } else {
                           // z = 0;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("finally");
                }
            }
        };
        timer.start();
    }
*/
    public void postdatatoserver(final String userstr){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Server_url1);
                    // Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
                    //InputStream caInput = new BufferedInputStream(new FileInputStream("android.resource://com.pkall/dolly/raw/cer.crt"));
                    InputStream caInput = context.getResources().openRawResource(R.raw.cer);
                    Certificate ca;
                    try {
                        ca = cf.generateCertificate(caInput);
                        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                    } finally {
                        caInput.close();
                    }

// Create a KeyStore containing our trusted CAs
                    String keyStoreType = KeyStore.getDefaultType();
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null, null);
                    keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
                    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                    tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, tmf.getTrustManagers(), null);

// Tell the URLConnection to use a SocketFactory from our SSLContext
                    //URL url = new URL("https://certs.cac.washington.edu/CAtest/");
                    HttpsURLConnection conn =
                            (HttpsURLConnection)url.openConnection();
                    conn.setSSLSocketFactory(context.getSocketFactory());

                    HostnameVerifier trustAllHostnames = new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true; // Just allow them all.
                        }
                    };
                    //conn.setDefaultHostnameVerifier(trustAllHostnames);
                    conn.setDefaultHostnameVerifier(
                            new HostnameVerifier() {
                                @Override
                                public boolean verify(final String Server_url1, final SSLSession session) {
                                    return true;
                                }
                            }
                    );

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("usertext", userstr);
                    jsonParam.put("imei", "");

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    private SSLContext trustCert() throws CertificateException,IOException,KeyStoreException,
            NoSuchAlgorithmException,KeyManagementException {
        AssetManager assetManager = getAssets();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca = cf.generateCertificate(assetManager.open("cer.crt"));

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        return context;
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //        usaidtext.setText(result.get(0));
                    int currentSize = msgs.length;
                    int newSize = currentSize + 1;
                    String[] tempArray = new String[ newSize ];
                    for (int i=0; i < currentSize; i++)
                    {
                        tempArray[i] = msgs [i];
                    }
                    tempArray[newSize- 1] = result.get(0);
                    msgs = tempArray;

                    Log.d(TAG, "onActivityResult: "+ result.get(0));


                    //msgs.add(result.get(0));
                    this.recyclerViewAdapter = new MyAdapter(this.getApplicationContext(), msgs, true);
                    this.recyclerView.setAdapter(recyclerViewAdapter);

                    classify(result.get(0));

                }
                break;
            }

        }
    }

    private void dollyreply(final String str1){
       // dollyprogress.setVisibility(View.VISIBLE);
        int currentSize = msgs.length;
        int newSize = currentSize + 1;
        String[] tempArray = new String[ newSize ];
        for (int i=0; i < currentSize; i++)
        {
            tempArray[i] = msgs [i];
        }
       // replystr = "Hmmmmm";
            replystr = str1;
        tempArray[newSize- 1] = replystr;
        msgs = tempArray;

        recyclerViewAdapter = new MyAdapter(context, msgs, false);
        recyclerView.setAdapter(recyclerViewAdapter);
        int bottom = recyclerView.getAdapter().getItemCount()-1;
        recyclerView.smoothScrollToPosition(bottom);

        new Timer().schedule(
                new TimerTask(){

                    @Override
                    public void run(){
                        //dollysaid.setText(str1);
                        //if you need some code to run when the delay expires
                        //dollyprogress.setVisibility(View.INVISIBLE);



                        //String data = editText.getText().toString();
                        //Log.i("TTS", "button clicked: " + data);
                        if(!dollymute) {
                            int speechStatus = t1.speak(replystr, TextToSpeech.QUEUE_FLUSH, null);

                            if (speechStatus == TextToSpeech.ERROR) {
                                Log.e("TTS", "Error in converting Text to Speech!");
                            }
                        }
                    }


                }, 500);


    }


    public void dollylisten(){
        int id;
        Thread timer = new Thread() {
            //int talk = 1;
            int z = 2, id;
            //Drawable d;
            public void run() {
                try {
                    sleep(50);
                    //      if(talk == 1){
                    for (z = 2; z < 167; z++) {
                        // while(state) {
                        //if (z < 130) {
                        sleep(80);
                        // z = z+1;
                        // sleep(pausetime);
                        if(state1) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // imageView.setImageResource(image[z]);
                                    id = getResources().getIdentifier("voice_animation" + z, "drawable", getPackageName());
                                    dollymouth.setImageResource(id);
                                }
                            });

                            if(z>=166)
                            {
                                z = 2;
                            }
                            Log.d("value of z", "aa" +z);

                        }

                        //}
                        //else {
                        //   z = 0;
                        //  }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    System.out.println("finally");
                }
            }
        };
        timer.start();
    }


    public void dollyface(){

        // Drawable [140] d;
        int id;

      //  for (int z = 0; z < image.length-1; z++) {

        //    id = getResources().getIdentifier("q" + z, "drawable", getPackageName());
            //d[z] = context.getDrawable(id);
            //d[z] =  ContextCompat.getDrawable(context, id);
            // int drawableId=getResources().getIdentifier("foo"+index, "drawable", getPackageName());
          //  Log.d("drawable", "value" + id);

      //  }

        Thread timer = new Thread() {
            //int talk = 1;
            int z = 10, id;
            //Drawable d;
            public void run() {
                try {
                    sleep(200);
                    //      if(talk == 1){
                    for (z = 10; z < 200; z++) {
                        // while(state) {
                        //if (z < 130) {
                        sleep(200);
                        // z = z+1;
                        // sleep(pausetime);
                        if(state1) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // imageView.setImageResource(image[z]);
                                    id = getResources().getIdentifier("q" + z, "drawable", getPackageName());
                                    imageViewface.setImageResource(id);
                                }
                            });

                            if(z>=130)
                            {
                                z = 10;
                            }
                            Log.d("value of z", "aa" +z);

                        }

                        //}
                        //else {
                        //   z = 0;
                        //  }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    System.out.println("finally");
                }
            }
        };
        timer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            dollymute = true;
            return true;
        }

        if (id == R.id.action_settings1) {
            dollymute = false;
            return true;
        }

        if (id == R.id.action_settings2) {
           // dollymute = false;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


