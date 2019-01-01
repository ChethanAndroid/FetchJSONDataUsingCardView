package com.example.chethan.fetchjsondatausingcardview;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;

    List<String> nameLst = new ArrayList<>();
    List<String> realnameLst = new ArrayList<>();
    List<String> teamLst = new ArrayList<>();
    List<String> firstappearanceLst = new ArrayList<>();
    List<String> createdbyLst = new ArrayList<>();
    List<String> publisherLst = new ArrayList<>();
    List<String> imageurlLst = new ArrayList<>();
    List<String> bioLst = new ArrayList<>();

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        nameLst.add("Thor");
//        nameLst.add("IronMan");
//        nameLst.add("BatMan");

        recyclerView = findViewById(R.id.rcv_layout_id);
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setNestedScrollingEnabled(false);

        new GetJSONData().execute();

//        adapter.notifyDataSetChanged();

    }

    public class GetJSONData extends AsyncTask<String,Integer,Boolean>{

        Boolean isSuccess = false;
        Boolean asdf = false;

        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
            progressDialog =  ProgressDialog.show(MainActivity.this,"Loading","Please wait",true);
        }



        @Override
        protected Boolean doInBackground(String... strings) {
            HttpURLConnection connection = null;
            StringBuilder builder = null;

            try {
                URL url = new URL(ServerLinkPojo.getMainurlLink());
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                builder = new StringBuilder();
                String line ="";

                while ((line = bufferedReader.readLine())!=null){
                    builder.append(line);
                    System.out.println("line:"+line);
                }
                inputStream.close();


                JSONArray jsonArray = new JSONArray(builder.toString());
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    nameLst.add(jsonObject.getString("name"));
                    realnameLst.add(jsonObject.getString("realname"));
                    teamLst.add(jsonObject.getString("team"));
                    firstappearanceLst.add(jsonObject.getString("firstappearance"));
                    createdbyLst.add(jsonObject.getString("createdby"));
                    publisherLst.add(jsonObject.getString("publisher"));
                    imageurlLst.add(jsonObject.getString("imageurl"));
                    bioLst.add(jsonObject.getString("bio"));


                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return asdf;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
            progressDialog.dismiss();

            adapter.notifyDataSetChanged();
        }
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.BaseHolder>{


        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.rcv_layout,viewGroup,false);
            BaseHolder baseHolder = new BaseHolder(view);
            return baseHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder bHolder, int i) {

//            bHolder.name.setText("Thor");
//            bHolder.realname.setText("Thor Odinson");
//            bHolder.team.setText("Avengers");
//            bHolder.createdBy.setText("Stan Lee");
//            bHolder.apperance.setText("1962");
//            bHolder.publisher.setText("Marvel Comics");
//            bHolder.bio.setText("\\r\\n\\t\\tThor's father Odin decides his son needed to be taught humility and consequently places" +
//                    " Thor (without memories of godhood) into the body and memories of an existing, partially disabled human medical student," +
//                    " Donald Blake.[52] After becoming a doctor and on vacation in Norway, Blake witnesses the arrival of an alien scouting party." +
//                    " Blake flees from the aliens into a cave. After discovering Thor's hammer Mjolnir (disguised as a walking stick) and striking it against a rock," +
//                    " he transforms into the thunder god.[53] Later, in Thor #159, Blake is revealed to have always been Thor, Odin's enchantment having caused him to" +
//                    " forget his history as The Thunder God and believe himself mortal.[54]\\r\\n\\t\\t");


            bHolder.name.setText(nameLst.get(i));
            bHolder.realname.setText(realnameLst.get(i));
            bHolder.team.setText(teamLst.get(i));
            bHolder.publisher.setText(publisherLst.get(i));
            bHolder.apperance.setText(firstappearanceLst.get(i));
            bHolder.createdBy.setText(createdbyLst.get(i));
//            bHolder.bio.setText(bioLst.get(i));


            Glide.with(MainActivity.this).load(imageurlLst.get(i))
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(bHolder.imageView);

//            Picasso.with(MainActivity.this)
//                    .load(imageurlLst.get(i))
//                    .into(bHolder.imageView);


            


        }

        @Override
        public int getItemCount() {
            return nameLst.size();
        }

        public class BaseHolder extends RecyclerView.ViewHolder{

            ImageView  imageView;
            TextView name,team,realname,publisher,bio,createdBy,apperance;

            public BaseHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.textView);
                team = itemView.findViewById(R.id.textView2);
                realname = itemView.findViewById(R.id.textView3);
                publisher = itemView.findViewById(R.id.textView4);
                createdBy = itemView.findViewById(R.id.textView5);
                apperance = itemView.findViewById(R.id.textView6);
                bio = itemView.findViewById(R.id.textView7);

                imageView = itemView.findViewById(R.id.imageView2);
            }
        }
    }

}

