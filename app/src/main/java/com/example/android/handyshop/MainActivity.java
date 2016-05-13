package com.example.android.handyshop;

import android.app.ActionBar;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends FragmentActivity {


    static Firebase handyShopDB;
    CollectionPagerAdapter myCollectionPagerAdapter;

    static ViewPager mViewPager;

    private GoogleApiClient client;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(myCollectionPagerAdapter);
        Firebase.setAndroidContext(this);
        handyShopDB = new Firebase("https://amber-torch-5366.firebaseio.com");
        handyShopDB.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (savedInstanceState == null) {
            mViewPager.setCurrentItem(1);
        }
}
        @Override
        public void onStart () {
            super.onStart();

            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            client.connect();
            Action viewAction = Action.newAction(
                    Action.TYPE_VIEW, // TODO: choose an action type.
                    "Main Page", // TODO: Define a title for the content shown.
                    // TODO: If you have web page content that matches this app activity's content,
                    // make sure this auto-generated web page URL is correct.
                    // Otherwise, set the URL to null.
                    Uri.parse("http://host/path"),
                    // TODO: Make sure this auto-generated app deep link URI is correct.
                    Uri.parse("android-app://com.example.android.handyshop/http/host/path")
            );
            AppIndex.AppIndexApi.start(client, viewAction);
        }

        @Override
        public void onStop () {
            super.onStop();

            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            Action viewAction = Action.newAction(
                    Action.TYPE_VIEW, // TODO: choose an action type.
                    "Main Page", // TODO: Define a title for the content shown.
                    // TODO: If you have web page content that matches this app activity's content,
                    // make sure this auto-generated web page URL is correct.
                    // Otherwise, set the URL to null.
                    Uri.parse("http://host/path"),
                    // TODO: Make sure this auto-generated app deep link URI is correct.
                    Uri.parse("android-app://com.example.android.handyshop/http/host/path")
            );
            AppIndex.AppIndexApi.end(client, viewAction);
            client.disconnect();
        }



        public static class CollectionPagerAdapter extends FragmentStatePagerAdapter {

            public CollectionPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int i) {
                Fragment fragment = null;
                //Bundle args=null;
                switch (i) {
                    case 0:
                        fragment = new ActivityFragment();

                        return fragment;
                    case 1:
                        fragment = new HomeFragment();
                        //args = new Bundle();
                        //args.putInt(HomeFragment.ARG_OBJECT, i);
                        //fragment.setArguments(args);
                        return fragment;
                    case 2:
                        fragment = new RequestsFragment();

                        return fragment;
                    case 3:
                        fragment = new OffersFragment();

                        return fragment;
                    case 4:
                        fragment = new InsertFragment();
                        return fragment;
                    default:
                        return fragment;
                }
            }

            @Override
            public int getCount() {
                // For this contrived example, we have a 100-object collection.
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Activity";

                    case 1:
                        return "Home";

                    case 2:
                        return "Requests";

                    case 3:
                        return "Offers";

                    case 4:
                        return "Insert";

                    default:
                        return "";

                }

            }
        }




        public static class HomeFragment extends Fragment {
            private int UserId = 0;
            public static final String ARG_OBJECT = "object";

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                Bundle args = getArguments();
                View rootView = null;

                rootView = inflater.inflate(R.layout.home, container, false);
                final Button button = (Button) rootView.findViewById(R.id.insert_button);
                button.setOnClickListener(insert);


                final Button button_1 = (Button) rootView.findViewById(R.id.request_button);
                button_1.setOnClickListener(request);

                return rootView;
            }


            View.OnClickListener insert = new View.OnClickListener() {
                public void onClick(View v) {
                    // do something here
                    mViewPager.setCurrentItem(4);
                }
            };

            View.OnClickListener request = new View.OnClickListener() {
                public void onClick(View v) {
                    Firebase ref = handyShopDB.child("requests");
                    Request req = new Request(UserId, "titolo", "categoria", "sottocategoria", "descrizione");
                    ref.push().setValue(req);
                }
            };


        }

        public static class RequestsFragment extends Fragment {
            SparseArray<Group> requestsList = new SparseArray<Group>();
            MyExpandableListAdapter adapter;


            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                Bundle args = getArguments();
                View rootView = null;
                rootView = inflater.inflate(R.layout.requests, container, false);
                //list = buildData();
                createData();
                ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
                adapter = new MyExpandableListAdapter(getActivity(), requestsList);
                listView.setAdapter(adapter);
                return rootView;
            }

            public void createData() {
                for (int j = 0; j < 5; j++) {
                    Group group = new Group("Test " + j);
                    for (int i = 0; i < 5; i++) {
                        group.children.add("Sub Item" + i);
                    }
                    requestsList.append(j, group);
                }
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {

                    Firebase ref = handyShopDB.child("requests");
                    Query queryRef = ref.orderByChild("userId").startAt(10);
                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            System.out.println("ciao");
                            for (DataSnapshot d : snapshot.getChildren()) {

                                String k = d.getKey();
                                Request req = d.getValue(Request.class);

                                //System.out.println(request.toString());
                                System.out.println(req.getTitle() + " - " + req.getCategory());
                                Group group = new Group(req.getCategory());
                                group.children.add(req.getDescription());
                                requestsList.append(requestsList.size(), group);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(FirebaseError f) {
                        }
                    });

                    // Get a reference to our posts
               /* Firebase ref = handyShopDB.child("requests");

                // Attach an listener to read the data at our posts reference
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        System.out.println("There are " + snapshot.getChildrenCount() + " Requests");

                        for (DataSnapshot d: snapshot.getChildren()){

                            String k = d.getKey();
                            Request req = d.getValue(Request.class);

                            //System.out.println(request.toString());
                            System.out.println(req.getTitle() + " - " + req.getCategory());

                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });*/

                }

            }
        }


        public static class OffersFragment extends Fragment {

            SparseArray<Group> offersList = new SparseArray<Group>();
            MyExpandableListAdapter adapter;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                Bundle args = getArguments();
                View rootView = inflater.inflate(R.layout.offers, container, false);

                createData();
                ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
                adapter = new MyExpandableListAdapter(getActivity(), offersList);
                listView.setAdapter(adapter);
                return rootView;
            }

            public void createData() {
                for (int j = 0; j < 5; j++) {
                    Group group = new Group("Test " + j);
                    for (int i = 0; i < 5; i++) {
                        group.children.add("Sub Item" + i);
                    }
                    offersList.append(j, group);
                }
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                if (isVisibleToUser) {
                    Group group = new Group("Leo");
                    group.children.add("cazzone");
                    offersList.append(offersList.size(), group);
                    adapter.notifyDataSetChanged();
                }
            }


        }

        public static class InsertFragment extends Fragment {

            public static final String ARG_OBJECT = "object";

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                Bundle args = getArguments();
                View rootView = null;
                rootView = inflater.inflate(R.layout.insert, container, false);

                return rootView;
            }


        }

        public static class ActivityFragment extends Fragment {

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                Bundle args = getArguments();
                View rootView = null;
                rootView = inflater.inflate(R.layout.insert, container, false);
                return rootView;
            }
        }
    }


