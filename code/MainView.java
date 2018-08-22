package com.great3.smartactivatedcollegewebbook;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class MainView extends AppCompatActivity {


    String types = "student";
    //Global String for user firebase link
    String department = "";
    String batchYear = "";
    String registerNumber = "";
    String name,address,email,gender,fathername,fatherphone,mothername,motherphone,phone,password_profile;

    ProgressDialog progressDialog;
    ListView circularList;
    ImageView addCircular;
    ArrayList<String> files = new ArrayList<>();


    //put extra for ebook for dept_library

    String dept_name="";

    String profile_filename= "";


    //Login ref
    Firebase mLoginRef;

    //Group ref

    Firebase Group_list;

    ListView group_list;
    ArrayAdapter<String> group_array_adapter;



    EditText parent_search;
    Button parent_search_button;


    //Profile List
    //  <<-- Start
    ImageView backFromProfileView;
    ImageView profileView;
    TextView profileName;
    TextView profileReg;
    TextView profileAddress;
    TextView profileBatch;
    TextView profileEmail;
    TextView profileFatherName;
    TextView profileFatherPhone;
    TextView profileMotherName;
    TextView profileMotherPhone;
    TextView profileGender;
    TextView profilePhone;

    // End-->>

    ImageView searchButton;
    EditText searchText;

    TextView userNameInSideMenu;
    TextView viewYourProfile;

    ImageView rotateImage;
    Bitmap pic = null;
    Uri imageUri = null;
    private static final int GALLERY_REQUEST = 1;
    private static final int PIC_CROP = 2;
    GestureDetectorCompat gestureDetectorCompat;
    LinearLayout menuView;
    ImageView menuButton;
    ImageView home;
    ImageView parent;
    ImageView map;
    ImageView academic;
    ImageView library;
    ImageView profile;
    ImageView addButtonInTimeline;
    ImageView backButtonAddTimeline;
    Button loginButton;
    ViewFlipper tabChanger;
    ViewFlipper layoutChanger;
    ImageView closeMenu;
    ProgressDialog mProgress;
    DatabaseReference titleAndDesc;

    StorageReference timelineStorage;
    ImageView timeline;
    ImageView menu;
    ImageView group;
    ImageView notification;
    int position = 1;
    int menuFlag=0;

    TextView homeText;
    TextView profileText;
    TextView academicText;
    TextView libraryText;
    TextView parentText;
    TextView mapText;
    boolean loggedInFlag = false;
    int HomepageFlag = 0;
    boolean menuSideFlag = false;
    boolean homepageFlag = false;

    //AddTimeline
    ImageView addImageOrVideo;
    EditText title;
    EditText desc;
    Button upload;

    //Login
    EditText loginUserName;
    EditText loginPassword;

    //academic

    TextView project_tab,placed_student_tab;


    //for card

    RecyclerView mRecyclerView;

    //Initialization for menu tab
    LinearLayout menuSideView;
    ListView menuSideListView;
    String web[] = {"Timeline","Group","Notification","Academic","Library","Map","Parents","PlacedStudentDetails","Help","Settings","Logout"};
    Integer imageId[] = {R.mipmap.ic_timeline_red, R.mipmap.ic_group_red, R.mipmap.ic_notification_red, R.mipmap.ic_academic_blue, R.mipmap.ic_library_blue, R.mipmap.ic_map_blue, R.mipmap.ic_parent_blue,android.R.drawable.ic_menu_help,android.R.drawable.ic_menu_help,android.R.drawable.ic_menu_edit,android.R.drawable.ic_menu_close_clear_cancel};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        startService(new Intent(getBaseContext(),SmartActivatedCollegeWebBookService.class));
        // Storage url for timeline
        timelineStorage = FirebaseStorage.getInstance().getReference();
        // Title and Description Url for timeline
        titleAndDesc = FirebaseDatabase.getInstance().getReference();


        // Progress for show progress like (please wait..) , (login..)
        mProgress = new ProgressDialog(this);

        backFromProfileView = (ImageView)findViewById(R.id.back_from_profile_view);

        userNameInSideMenu = (TextView)findViewById(R.id.username_in_side_menu);
        viewYourProfile = (TextView)findViewById(R.id.view_your_profile);

        searchButton = (ImageView)findViewById(R.id.search_button);
        searchText = (EditText)findViewById(R.id.search_edittext);

        //acedamic

        project_tab = (TextView)findViewById(R.id.project_tab);
        placed_student_tab = (TextView)findViewById(R.id.placed_student_tab);

        //ProfileView
        profileView = (ImageView) findViewById(R.id.profile_image);
        profileName = (TextView)findViewById(R.id.profile_view_name);
        profileReg = (TextView)findViewById(R.id.profile_view_reg);
        profileAddress = (TextView)findViewById(R.id.profile_view_address);
        profileBatch =(TextView)findViewById(R.id.profile_view_batch);
        profileEmail = (TextView)findViewById(R.id.profile_view_email);
        profileFatherName = (TextView)findViewById(R.id.profile_view_father_name);
        profileFatherPhone = (TextView)findViewById(R.id.profile_view_father_phone);
        profileMotherName = (TextView)findViewById(R.id.profile_view_mother_name);
        profileMotherPhone = (TextView)findViewById(R.id.profile_view_mother_phone);
        profileGender = (TextView)findViewById(R.id.profile_view_gender);
        profilePhone = (TextView)findViewById(R.id.profile_view_phone);

        //Group_list

        group_list = (ListView)findViewById(R.id.group_list);


        // Rotate image in addToTimeline
        rotateImage = (ImageView)findViewById(R.id.rotate_image);

        // Close menu for small menu
        closeMenu = (ImageView)findViewById(R.id.close_menu);
        // Please Don't ask and Don't touch recycler view
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ViewFlipper for mainView Layout
        layoutChanger = (ViewFlipper)findViewById(R.id.layout_changer);

        // ViewFlipper for Homepage Layout
        tabChanger = (ViewFlipper)findViewById(R.id.tab_changer);

        loginButton = (Button)findViewById(R.id.login_button);

        backButtonAddTimeline = (ImageView)findViewById(R.id.back_button_add_timeline);
        addButtonInTimeline = (ImageView)findViewById(R.id.add_button_in_timeline);

        addImageOrVideo = (ImageView)findViewById(R.id.imageorvideo_for_upload);
        title = (EditText) findViewById(R.id.title);
        desc = (EditText)findViewById(R.id.desc);

        profile = (ImageView)findViewById(R.id.profile);
        library = (ImageView)findViewById(R.id.library);
        academic = (ImageView)findViewById(R.id.academic);
        map = (ImageView)findViewById(R.id.map);
        parent = (ImageView)findViewById(R.id.parent);
        home = (ImageView)findViewById(R.id.home);
        menuButton = (ImageView)findViewById(R.id.menu_button);
        menuView = (LinearLayout) findViewById(R.id.menu_view);
        timeline = (ImageView)findViewById(R.id.timeline);
        menu = (ImageView)findViewById(R.id.menu);
        group = (ImageView)findViewById(R.id.group);
        notification = (ImageView)findViewById(R.id.notification);
        upload = (Button)findViewById(R.id.upload_button);

        homeText = (TextView)findViewById(R.id.home_text);
        profileText = (TextView)findViewById(R.id.profile_text);
        academicText = (TextView)findViewById(R.id.academic_text);
        parentText = (TextView)findViewById(R.id.parents_text);
        libraryText = (TextView)findViewById(R.id.library_text);
        mapText = (TextView)findViewById(R.id.map_text);

        menuSideView = (LinearLayout)findViewById(R.id.menu_side_view);
        menuSideListView = (ListView)findViewById(R.id.menu_side_list_view);

        //Login EditText
        loginUserName = (EditText)findViewById(R.id.login_username);
        loginPassword = (EditText)findViewById(R.id.login_password);

        parent_search = (EditText)findViewById(R.id.parent_search);
        parent_search_button = (Button)findViewById(R.id.parent_search_button);



        //marshmallow permission
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //academic

        placed_student_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                startActivity(new Intent(getApplicationContext(),PlacedStudentDetails.class));
            }
        });

        project_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                startActivity(new Intent(getApplicationContext(),ProjectDetails.class));

            }
        });

        parent_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String reg = parent_search.getText().toString();

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                startActivity(new Intent(getApplicationContext(),Complaints.class).putExtra("reg",reg));

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                String text = searchText.getText().toString();
                String arr[]=text.split("-");

                if(arr.length==3)
                {
                    String reg = arr[0];
                    String dept = arr[1];
                    String batch = arr[2];
                    int count = 0;
                    for(int i=0;i<batch.length();i++)
                    {
                        char ch = batch.charAt(i);
                        if(Character.isDigit(ch))
                        {
                            count++;
                        }
                    }
                    if(count == batch.length())
                    {
                        int bt = Integer.parseInt(batch);
                        Calendar c = Calendar.getInstance();
                        int yr = c.get(Calendar.YEAR);
                        if (reg.length() == 7 && (dept.equals("CSE") || dept.equals("CIVIL") || dept.equals("MECH") ||dept.equals("ECE") ||dept.equals("EEE") ||dept.equals("NANO") ||dept.equals("TEXTILE") ||dept.equals("BIO") ||dept.equals("FOOD") || dept.equals("MCT") ||dept.equals("IT") ||dept.equals("EI") ) && bt >=2014 &&bt<=yr)
                        {
                            startActivity(new Intent(getApplicationContext(),ShowSearch.class).putExtra("reg",reg).putExtra("dept",dept).putExtra("batch",batch));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Enter valid text - Eg. 1414114-CSE-2014", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Enter valid text - Eg. 1414114-CSE-2014", Toast.LENGTH_SHORT).show();
                    }



                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Enter valid text - Eg. 1414114-CSE-2014", Toast.LENGTH_SHORT).show();
                }


            }
        });


        //Firebase References
        mLoginRef = new Firebase("https://sacw-signup.firebaseio.com/account/");


        gestureDetectorCompat = new GestureDetectorCompat(getApplicationContext(),new GestureClass());



	 progressDialog = new ProgressDialog(MainView.this);
        circularList = (ListView)findViewById(R.id.circular_list);
        addCircular = (ImageView)findViewById(R.id.add_circular);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,files);
        circularList.setAdapter(arrayAdapter);

        Firebase mRef = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/title/");
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String tit = dataSnapshot.getValue(String.class);
                files.add(tit);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        addCircular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                Intent  i = new Intent(getApplicationContext(),add_new_circular.class);
                startActivity(i);

            }
        });

        circularList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                final String title = (String)circularList.getItemAtPosition(position);
                Firebase mRef = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/file/"+title+"/downloadUrl");
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String url = dataSnapshot.getValue(String.class);

                        Firebase mRefFileName = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/file/"+title+"/filename");
                        mRefFileName.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String fileName = dataSnapshot.getValue(String.class);


                                File dir = new File(Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/Circular/");
                                if(!dir.exists())
                                {
                                    dir.mkdirs();
                                }
                                File file = new File(dir,fileName);

                                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);

                                storageReference.getFile(file).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Fail to download",Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Downloaded Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                                progressDialog.dismiss();
                            }
                        });



                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                        progressDialog.dismiss();
                    }
                });


            }
        });


        // To view the user profile in the side menu
        viewYourProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                timeline.setImageResource(R.mipmap.ic_timeline_green);
                menu.setImageResource(R.mipmap.ic_menu_green);
                group.setImageResource(R.mipmap.ic_group_green);
                notification.setImageResource(R.mipmap.ic_notification_red);
                menuSideView.setVisibility(View.INVISIBLE);
                menuView.setVisibility(View.INVISIBLE);
                menuSideFlag = false;
                menuButton.setVisibility(View.VISIBLE);
                tabChanger.setDisplayedChild(2);
                setTabPosition(2);

                layoutChanger.setDisplayedChild(4);
                menuButton.setVisibility(View.INVISIBLE);
                homepageFlag = false;
                setProfile();

            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                layoutChanger.setDisplayedChild(4);
                menuView.setVisibility(View.INVISIBLE);
                menuButton.setVisibility(View.VISIBLE);
                menuFlag=0;

                homeText.setTextColor(Color.parseColor("#FF9912F8"));
                profileText.setTextColor(Color.parseColor("#FFEE2419"));
                libraryText.setTextColor(Color.parseColor("#FF9912F8"));
                mapText.setTextColor(Color.parseColor("#FF9912F8"));
                parentText.setTextColor(Color.parseColor("#FF9912F8"));
                academicText.setTextColor(Color.parseColor("#FF9912F8"));

                menuButton.setVisibility(View.VISIBLE);
                menuView.setVisibility(View.INVISIBLE);
                layoutChanger.setDisplayedChild(4);
                homepageFlag = false;

            }
        });

        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                menuView.setVisibility(View.INVISIBLE);
                menuButton.setVisibility(View.VISIBLE);
                menuFlag=0;

                homeText.setTextColor(Color.parseColor("#FF9912F8"));
                profileText.setTextColor(Color.parseColor("#FF9912F8"));
                libraryText.setTextColor(Color.parseColor("#FFEE2419"));
                mapText.setTextColor(Color.parseColor("#FF9912F8"));
                parentText.setTextColor(Color.parseColor("#FF9912F8"));
                academicText.setTextColor(Color.parseColor("#FF9912F8"));

                menuButton.setVisibility(View.VISIBLE);
                menuView.setVisibility(View.INVISIBLE);
                layoutChanger.setDisplayedChild(8);
                homepageFlag = false;

            }
        });

        academic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                menuView.setVisibility(View.INVISIBLE);
                menuButton.setVisibility(View.VISIBLE);
                menuFlag=0;

                homeText.setTextColor(Color.parseColor("#FF9912F8"));
                profileText.setTextColor(Color.parseColor("#FF9912F8"));
                libraryText.setTextColor(Color.parseColor("#FF9912F8"));
                mapText.setTextColor(Color.parseColor("#FF9912F8"));
                parentText.setTextColor(Color.parseColor("#FF9912F8"));
                academicText.setTextColor(Color.parseColor("#FFEE2419"));

                menuButton.setVisibility(View.VISIBLE);
                menuView.setVisibility(View.INVISIBLE);
                layoutChanger.setDisplayedChild(5);
                homepageFlag = false;


            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                menuView.setVisibility(View.INVISIBLE);
                menuButton.setVisibility(View.VISIBLE);
                menuFlag=0;

                homeText.setTextColor(Color.parseColor("#FF9912F8"));
                profileText.setTextColor(Color.parseColor("#FF9912F8"));
                libraryText.setTextColor(Color.parseColor("#FF9912F8"));
                mapText.setTextColor(Color.parseColor("#FFEE2419"));
                parentText.setTextColor(Color.parseColor("#FF9912F8"));
                academicText.setTextColor(Color.parseColor("#FF9912F8"));

                menuButton.setVisibility(View.VISIBLE);
                menuView.setVisibility(View.INVISIBLE);
                layoutChanger.setDisplayedChild(6);
                homepageFlag = false;


            }
        });

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                menuView.setVisibility(View.INVISIBLE);
                menuButton.setVisibility(View.VISIBLE);
                menuFlag=0;

                homeText.setTextColor(Color.parseColor("#FF9912F8"));
                profileText.setTextColor(Color.parseColor("#FF9912F8"));
                libraryText.setTextColor(Color.parseColor("#FF9912F8"));
                mapText.setTextColor(Color.parseColor("#FF9912F8"));
                parentText.setTextColor(Color.parseColor("#FFEE2419"));
                academicText.setTextColor(Color.parseColor("#FF9912F8"));

                menuButton.setVisibility(View.VISIBLE);
                menuView.setVisibility(View.INVISIBLE);
                layoutChanger.setDisplayedChild(7);
                homepageFlag = false;

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                menuView.setVisibility(View.INVISIBLE);
                menuButton.setVisibility(View.VISIBLE);
                menuFlag=0;
                homepageFlag=true;
                homeText.setTextColor(Color.parseColor("#FFEE2419"));
                profileText.setTextColor(Color.parseColor("#FF9912F8"));
                libraryText.setTextColor(Color.parseColor("#FF9912F8"));
                mapText.setTextColor(Color.parseColor("#FF9912F8"));
                parentText.setTextColor(Color.parseColor("#FF9912F8"));
                academicText.setTextColor(Color.parseColor("#FF9912F8"));

                menuButton.setVisibility(View.VISIBLE);
                menuView.setVisibility(View.INVISIBLE);
                layoutChanger.setDisplayedChild(2);
                homepageFlag = true;


            }
        });


        // code for 3sec for logo visibility
        new CountDownTimer(3000, 1000) {
            public void onFinish() {

                // code for change the layout to login or homepage


                    File profile_path = new File (Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/Profile/");
                    if (!profile_path.exists())
                    {
                        profile_path.mkdirs();
                    }

                    File file = new File(profile_path,"profile.txt");
                if(!file.exists())
                {
                    //if not logged in
                    layoutChanger.setDisplayedChild(1);
                }
                else
                {
                    try {

                        FileInputStream fis = new FileInputStream(file);
                        DataInputStream in = new DataInputStream(fis);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String reg= br.readLine();
                        Toast.makeText(getApplicationContext(),reg,Toast.LENGTH_LONG).show();

                        if(reg.equals(null))
                        {
                            layoutChanger.setDisplayedChild(1);
                        }
                        else
                        {

                            //if already logged in

                            String dept = "";
                            String btch = "";
                            String strLine="";
                            int index = 0;
                            while ((strLine = br.readLine()) != null) {

                                if(index==2)
                                dept = strLine;
                                if(index==3)
                                btch = strLine;
                                index++;
                            }

                            File file1 = new File(profile_path,"type.txt");
                            if(file1.exists())
                            {
                                FileInputStream fis1 = new FileInputStream(file1);
                                DataInputStream in1 = new DataInputStream(fis1);
                                BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
                                String type= br1.readLine();
                                types = type;
                                if(types.equals("student"))
                                hide();
                            }


                            Toast.makeText(getApplicationContext(),dept+" "+btch,Toast.LENGTH_LONG).show();
                            department = dept;
                            batchYear = btch;
                            registerNumber = reg;
                            userNameInSideMenu.setText(registerNumber);

                            layoutChanger.setDisplayedChild(2);
                            HomepageFlag = 1;
                            homeText.setTextColor(Color.parseColor("#FFEE2419"));
                            profileText.setTextColor(Color.parseColor("#FF9912F8"));
                            libraryText.setTextColor(Color.parseColor("#FF9912F8"));
                            mapText.setTextColor(Color.parseColor("#FF9912F8"));
                            parentText.setTextColor(Color.parseColor("#FF9912F8"));
                            academicText.setTextColor(Color.parseColor("#FF9912F8"));
                            timeline.setImageResource(R.mipmap.ic_timeline_red);
                            menu.setImageResource(R.mipmap.ic_menu_green);
                            group.setImageResource(R.mipmap.ic_group_green);
                            notification.setImageResource(R.mipmap.ic_notification_green);
                            menuButton.setVisibility(View.VISIBLE);
                            menuSideFlag = false;
                            homepageFlag = true;
                            loggedInFlag = true;

                            CustomList adapter = new CustomList(MainView.this,web,imageId);
                            menuSideListView.setAdapter(adapter);
                            timelineStart();
                        }

                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(),"File not found1",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }



                }





            }
            public void onTick(long millisUntilFinished) {
            }
        }.start();

        backFromProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                layoutChanger.setDisplayedChild(2);
                menuButton.setVisibility(View.VISIBLE);
                homepageFlag = true;

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                progressDialog.setMessage("Please wait..");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                mLoginRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String,String> container = dataSnapshot.getValue(Map.class);

                        final String userName = loginUserName.getText().toString();
                        final String mPassword = loginPassword.getText().toString();

                        if(container.containsValue(userName))
                        {

                            Firebase mLoginDetails = new Firebase("https://sacw-signup.firebaseio.com/login/"+userName);
                            mLoginDetails.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Map<String,String> details = dataSnapshot.getValue(Map.class);
                                    String batch = details.get("batch");
                                    final String password = details.get("password");
                                    String dept = details.get("dept");

                                    if(mPassword.equals(password))
                                    {
                                        department = dept;
                                        batchYear = batch;
                                        registerNumber = userName;
                                        userNameInSideMenu.setText(registerNumber);

                                        layoutChanger.setDisplayedChild(2);
                                        HomepageFlag = 1;
                                        homeText.setTextColor(Color.parseColor("#FFEE2419"));
                                        profileText.setTextColor(Color.parseColor("#FF9912F8"));
                                        libraryText.setTextColor(Color.parseColor("#FF9912F8"));
                                        mapText.setTextColor(Color.parseColor("#FF9912F8"));
                                        parentText.setTextColor(Color.parseColor("#FF9912F8"));
                                        academicText.setTextColor(Color.parseColor("#FF9912F8"));
                                        timeline.setImageResource(R.mipmap.ic_timeline_red);
                                        menu.setImageResource(R.mipmap.ic_menu_green);
                                        group.setImageResource(R.mipmap.ic_group_green);
                                        notification.setImageResource(R.mipmap.ic_notification_green);
                                        menuButton.setVisibility(View.VISIBLE);
                                        menuSideFlag = false;
                                        homepageFlag = true;
                                        loggedInFlag = true;

                                        CustomList adapter = new CustomList(MainView.this,web,imageId);
                                        menuSideListView.setAdapter(adapter);
                                        progressDialog.dismiss();
                                        timelineStart();


                                     //Firebase profile = new Firebase("\"https://sacw-signup.firebaseio.com/"+department);


                                        /*profile_details.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                                Map<String,String> profile = dataSnapshot.getValue(Map.class);
                                                 name = profile.get("name");
                                                 address = profile.get("address");
                                                 email = profile.get("email");
                                                 gender = profile.get("gender");
                                                 fathername = profile.get("fathername");
                                                 fatherphone = profile.get("fatherphone");
                                                 mothername = profile.get("mothername");
                                                 motherphone = profile.get("motherphone");
                                                 phone = profile.get("phone");
                                                 password_profile = profile.get("password");
                                            }

                                            @Override
                                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                                            }

                                            @Override
                                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });*/

                                        Toast.makeText(getApplicationContext(),"Welcome "+userName,Toast.LENGTH_LONG).show();

                                        startService(new Intent(getBaseContext(),SmartActivatedCollegeWebBookService.class));
                                        profile();
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Invalid User name",Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });



            }
        });
        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                changeTab(v);
            }
        });
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                changeTab(v);
                group_chat();
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                Firebase mRefCount = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/circularcount");
                mRefCount.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String count = dataSnapshot.getValue(String.class);
                        File dir = new File(Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/.cir/");
                        if(!dir.exists())
                        {
                            dir.mkdirs();
                        }
                        File file = new File(dir,"count.txt");
                        if(file.exists()) {

                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(count.getBytes());
                                fos.close();
                            } catch (IOException e) {
                                Toast.makeText(getApplicationContext(),"File not found1",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        }
                        else
                        {

                            try {
                                file.createNewFile();
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(count.getBytes());
                                fos.close();
                            } catch (IOException e) {
                                Toast.makeText(getApplicationContext(),"File not found1",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                changeTab(v);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                changeTab(v);
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                if(menuFlag==0)
                {
                    addButtonInTimeline.setVisibility(View.INVISIBLE);
                    menuView.setVisibility(View.VISIBLE);
                    menuFlag=1;
                    menuButton.setVisibility(View.INVISIBLE);
                }

            }
        });

        menuSideListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Toast.makeText(getApplicationContext(), "You clicked " + web[position], Toast.LENGTH_SHORT).show();
                String select = web[position];
                switch (select)
                {
                    case "Timeline":

                        //vibrator
                        Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(50);

                        timeline.setImageResource(R.mipmap.ic_timeline_red);
                        menu.setImageResource(R.mipmap.ic_menu_green);
                        group.setImageResource(R.mipmap.ic_group_green);
                        notification.setImageResource(R.mipmap.ic_notification_green);
                        menuSideView.setVisibility(View.INVISIBLE);
                        menuButton.setVisibility(View.VISIBLE);
                        menuView.setVisibility(View.INVISIBLE);
                        if(types.equals("staff")) {
                            addButtonInTimeline.setVisibility(View.VISIBLE);
                        }menuSideFlag = false;
                        tabChanger.setDisplayedChild(0);
                        setTabPosition(0);
                        break;
                    case "Group":

                        //vibrator
                        Vibrator vi = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        vi.vibrate(50);

                        timeline.setImageResource(R.mipmap.ic_timeline_green);
                        menu.setImageResource(R.mipmap.ic_menu_green);
                        group.setImageResource(R.mipmap.ic_group_red);
                        notification.setImageResource(R.mipmap.ic_notification_green);
                        menuSideView.setVisibility(View.INVISIBLE);
                        menuButton.setVisibility(View.VISIBLE);
                        menuView.setVisibility(View.INVISIBLE);
                        menuSideFlag = false;
                        tabChanger.setDisplayedChild(1);
                        setTabPosition(1);
                        break;
                    case "Notification":

                        //vibrator
                        Vibrator vb = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        vb.vibrate(50);

                        timeline.setImageResource(R.mipmap.ic_timeline_green);
                        menu.setImageResource(R.mipmap.ic_menu_green);
                        group.setImageResource(R.mipmap.ic_group_green);
                        notification.setImageResource(R.mipmap.ic_notification_red);
                        menuSideView.setVisibility(View.INVISIBLE);
                        menuView.setVisibility(View.INVISIBLE);
                        menuSideFlag = false;
                        menuButton.setVisibility(View.VISIBLE);
                        tabChanger.setDisplayedChild(2);
                        Firebase mRefCount = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/circularcount");
                        mRefCount.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String count = dataSnapshot.getValue(String.class);
                                File dir = new File(Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/.cir/");
                                if(!dir.exists())
                                {
                                    dir.mkdirs();
                                }
                                File file = new File(dir,"count.txt");
                                if(file.exists()) {

                                    try {
                                        FileOutputStream fos = new FileOutputStream(file);
                                        fos.write(count.getBytes());
                                        fos.close();
                                    } catch (IOException e) {
                                        Toast.makeText(getApplicationContext(),"File not found1",Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }

                                }
                                else
                                {

                                    try {
                                        file.createNewFile();
                                        FileOutputStream fos = new FileOutputStream(file);
                                        fos.write(count.getBytes());
                                        fos.close();
                                    } catch (IOException e) {
                                        Toast.makeText(getApplicationContext(),"File not found1",Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        setTabPosition(2);
                        break;
                    case "Academic":

                        //vibrator
                        Vibrator ad = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        ad.vibrate(50);

                        menuView.setVisibility(View.INVISIBLE);
                        menuButton.setVisibility(View.VISIBLE);
                        menuFlag=0;

                        homeText.setTextColor(Color.parseColor("#FF9912F8"));
                        profileText.setTextColor(Color.parseColor("#FF9912F8"));
                        libraryText.setTextColor(Color.parseColor("#FF9912F8"));
                        mapText.setTextColor(Color.parseColor("#FF9912F8"));
                        parentText.setTextColor(Color.parseColor("#FF9912F8"));
                        academicText.setTextColor(Color.parseColor("#FFEE2419"));

                        menuButton.setVisibility(View.VISIBLE);
                        menuView.setVisibility(View.INVISIBLE);
                        layoutChanger.setDisplayedChild(5);
                        homepageFlag = false;
                        break;
                    case "Library":

                        //vibrator
                        Vibrator lib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        lib.vibrate(50);

                        menuView.setVisibility(View.INVISIBLE);
                        menuButton.setVisibility(View.VISIBLE);
                        menuFlag=0;

                        homeText.setTextColor(Color.parseColor("#FF9912F8"));
                        profileText.setTextColor(Color.parseColor("#FF9912F8"));
                        libraryText.setTextColor(Color.parseColor("#FFEE2419"));
                        mapText.setTextColor(Color.parseColor("#FF9912F8"));
                        parentText.setTextColor(Color.parseColor("#FF9912F8"));
                        academicText.setTextColor(Color.parseColor("#FF9912F8"));

                        menuButton.setVisibility(View.VISIBLE);
                        menuView.setVisibility(View.INVISIBLE);
                        layoutChanger.setDisplayedChild(8);
                        homepageFlag = false;
                        break;
                    case "Map":

                        //vibrator
                        Vibrator m = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        m.vibrate(50);

                        Toast.makeText(getApplicationContext(),"Coming soon..",Toast.LENGTH_LONG).show();
                        break;
                    case "Parents":

                        //vibrator
                        Vibrator p = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        p.vibrate(50);

                        Toast.makeText(getApplicationContext(),"Coming soon..",Toast.LENGTH_LONG).show();
                        break;
                    case "PlacedStudentDetails":

                        //vibrator
                        Vibrator ps = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        ps.vibrate(50);

                        startActivity(new Intent(MainView.this,PlacedStudentDetails.class));
                        break;
                    case "Help":

                        //vibrator
                        Vibrator h = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        h.vibrate(50);

                        Toast.makeText(getApplicationContext(),"Coming soon..",Toast.LENGTH_LONG).show();
                        break;
                    case "Settings":

                        //vibrator
                        Vibrator s = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        s.vibrate(50);

                        Toast.makeText(getApplicationContext(),"Coming soon..",Toast.LENGTH_LONG).show();
                        break;
                    case "Logout":

                        //vibrator
                        Vibrator l = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        l.vibrate(50);

                        File profile_path = new File (Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/Profile/");
                        if (!profile_path.exists())
                        {
                            profile_path.mkdirs();
                        }
                        File file = new File(profile_path,"profile.txt");
                        file.delete();
                        startActivity(new Intent(getApplicationContext(),MainView.class));
                        break;
                }






            }
        });

        backButtonAddTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                layoutChanger.setDisplayedChild(2);
                menuButton.setVisibility(View.VISIBLE);
                homepageFlag = true;
                title.setText("");
                desc.setText("");
                addImageOrVideo.setImageResource(R.mipmap.click_add);
                pic = null;
                imageUri = null;

            }
        });

        addButtonInTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                layoutChanger.setDisplayedChild(3);
                menuButton.setVisibility(View.INVISIBLE);
                homepageFlag = false;

            }
        });

        addImageOrVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                startPosting();
            }
        });

        closeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                menuButton.setVisibility(View.VISIBLE);
                menuView.setVisibility(View.INVISIBLE);
                if(types.equals("staff")) {
                    addButtonInTimeline.setVisibility(View.VISIBLE);
                }menuFlag=0;
            }
        });

        rotateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                if(pic==null)
                {
                    Toast.makeText(getApplicationContext(),"Please select the image",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotated = Bitmap.createBitmap(pic,0,0,pic.getWidth(),pic.getHeight(),matrix,true);
                    addImageOrVideo.setImageBitmap(rotated);
                    pic = rotated;
                }

            }
        });

        group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                String group_position = (String)group_list.getItemAtPosition(position);

                Intent i = new Intent(getApplicationContext(),Group_Chat_Area.class);
                i.putExtra("group",group_position);
                i.putExtra("register",registerNumber);
                startActivity(i);

                Toast.makeText(getApplicationContext(),group_position,Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void hide()
    {

        addButtonInTimeline.setVisibility(View.INVISIBLE);
        addCircular.setVisibility(View.INVISIBLE);

    }

    private void profile() {



            Firebase acc = new Firebase("https://sacw-signup.firebaseio.com/KSRCOLLEGEOFTECHNOLOGY/"+department+"/"+batchYear+"/"+registerNumber+"/");

            acc.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Map<String,String> map = dataSnapshot.getValue(Map.class);

                    try {

                        File profile_path = new File (Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/Profile/");
                    if (!profile_path.exists())
                    {
                        profile_path.mkdirs();
                    }

                    File file = new File(profile_path,"profile.txt");
                        File file1 = new File(profile_path,"type.txt");
                    String val = registerNumber+"\n"+map.get("name")+"\n"+map.get("address")+"\n"+department+"\n"+map.get("batch")+"\n"+map.get("email")+
                            "\n"+map.get("fathername")+"\n"+map.get("fatherphone")+"\n"+map.get("mothername")+"\n"+map.get("motherphone")+
                            "\n"+map.get("gender")+"\n"+map.get("phone");
                    String ty = map.get("type");
                        if(!file.exists()) {
                            file.createNewFile();
                        }
                        if(!file1.exists())
                        {
                            file1.createNewFile();
                        }
                        FileOutputStream os1 = new FileOutputStream(file1);
                        os1.write(ty.getBytes());
                        types = ty;
                        if(types.equals("student"))
                            hide();
                    FileOutputStream os = new FileOutputStream(file);
                    os.write(val.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


    }



/*
    public void performCrop()
    {
        try
        {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(imageUri,"image
");
cropIntent.putExtra("crop","true");
            cropIntent.putExtra("aspectX",1);
            cropIntent.putExtra("aspectY",1);
            cropIntent.putExtra("outputX",640);
            cropIntent.putExtra("outputY",320);
            cropIntent.putExtra("return-data",true);


            startActivityForResult(cropIntent,PIC_CROP);

        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(getApplicationContext(),"your device doesn't support crop",Toast.LENGTH_LONG).show();
        }
    }
*/


    public void startPosting()
    {
        mProgress.setMessage("Uploading...");
        mProgress.show();
        final String title_text = title.getText().toString().trim();
        final String desc_text = desc.getText().toString().trim();

        if(!TextUtils.isEmpty(title_text)&&!TextUtils.isEmpty(desc_text))
        {

            Bitmap bitmap = ((BitmapDrawable)addImageOrVideo.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            StorageReference filePath = timelineStorage.child(System.currentTimeMillis()+"");

            filePath.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String folder = System.currentTimeMillis()+"";
                    titleAndDesc.child(folder).child("title").setValue(title_text);
                    titleAndDesc.child(folder).child("desc").setValue(desc_text);
                    titleAndDesc.child(folder).child("image").setValue(downloadUrl.toString());
                    mProgress.dismiss();
                    layoutChanger.setDisplayedChild(2);
                    menuButton.setVisibility(View.VISIBLE);
                    homepageFlag = true;
                    title.setText("");
                    desc.setText("");
                    addImageOrVideo.setImageResource(R.mipmap.click_add);
                    pic = null;
                    imageUri = null;
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please enter all details",Toast.LENGTH_SHORT).show();
            mProgress.dismiss();
        }

    }

    public void changeTab(View view)
    {

        if(view == findViewById(R.id.timeline))
        {
            setTabPosition(1);
            timeline.setImageResource(R.mipmap.ic_timeline_red);
            menu.setImageResource(R.mipmap.ic_menu_green);
            group.setImageResource(R.mipmap.ic_group_green);
            notification.setImageResource(R.mipmap.ic_notification_green);
            menuSideView.setVisibility(View.INVISIBLE);
            menuButton.setVisibility(View.VISIBLE);
            menuSideFlag = false;
            menuView.setVisibility(View.INVISIBLE);
            if(types.equals("staff")) {
                addButtonInTimeline.setVisibility(View.VISIBLE);
            }
            tabChanger.setDisplayedChild(0);

        }
        if(view == findViewById(R.id.group))
        {
            setTabPosition(2);
            menuSideFlag = false;
            timeline.setImageResource(R.mipmap.ic_timeline_green);
            menu.setImageResource(R.mipmap.ic_menu_green);
            group.setImageResource(R.mipmap.ic_group_red);
            notification.setImageResource(R.mipmap.ic_notification_green);
            menuSideView.setVisibility(View.INVISIBLE);
            menuButton.setVisibility(View.VISIBLE);
            menuView.setVisibility(View.INVISIBLE);
            tabChanger.setDisplayedChild(1);
            group_chat();
        }
        if(view == findViewById(R.id.notification))
        {
            setTabPosition(3);
            Firebase mRefCount = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/circularcount");
            mRefCount.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String count = dataSnapshot.getValue(String.class);
                    File dir = new File(Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/.cir/");
                    if(!dir.exists())
                    {
                        dir.mkdirs();
                    }
                    File file = new File(dir,"count.txt");
                    if(file.exists()) {

                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(count.getBytes());
                            fos.close();
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(),"File not found1",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                    else
                    {

                        try {
                            file.createNewFile();
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(count.getBytes());
                            fos.close();
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(),"File not found1",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            menuSideFlag = false;
            timeline.setImageResource(R.mipmap.ic_timeline_green);
            menu.setImageResource(R.mipmap.ic_menu_green);
            group.setImageResource(R.mipmap.ic_group_green);
            notification.setImageResource(R.mipmap.ic_notification_red);
            menuSideView.setVisibility(View.INVISIBLE);
            menuButton.setVisibility(View.VISIBLE);
            menuView.setVisibility(View.INVISIBLE);
            tabChanger.setDisplayedChild(2);
        }
        if(view == findViewById(R.id.menu))
        {
            setTabPosition(4);
            menuSideFlag = true;
            timeline.setImageResource(R.mipmap.ic_timeline_green);
            menu.setImageResource(R.mipmap.ic_menu_red);
            group.setImageResource(R.mipmap.ic_group_green);
            notification.setImageResource(R.mipmap.ic_notification_green);
            tabChanger.setDisplayedChild(3);
            menuButton.setVisibility(View.INVISIBLE);
            menuSideView.setVisibility(View.VISIBLE);
            menuView.setVisibility(View.INVISIBLE);

        }
    }

    public void incrementTabPosition()
    {
        if(this.position!=4)
            this.position++;
    }
    public void decrementTabPosition()
    {
        if(this.position!=1)
            this.position--;
    }

    public void setTabPosition(int pos)
    {
        this.position = pos;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        menuView.setVisibility(View.INVISIBLE);
        menuFlag=0;
        if(!menuSideFlag&&homepageFlag) {
            menuButton.setVisibility(View.VISIBLE);
            if(types.equals("staff")) {
                addButtonInTimeline.setVisibility(View.VISIBLE);
            }
        }
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void library_cse_btn(View view) {

        dept_name = "cse";
        Intent i = new Intent(getApplicationContext(),add_book_library.class);
        i.putExtra("dept_name",dept_name);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"CSE",Toast.LENGTH_SHORT).show();
    }

    public void library_ece_btn(View view) {

        dept_name = "ece";
        Intent i = new Intent(getApplicationContext(),add_book_library.class);
        i.putExtra("dept_name",dept_name);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"ECE",Toast.LENGTH_SHORT).show();
    }

    public void library_eee_btn(View view) {

        dept_name = "eee";
        Intent i = new Intent(getApplicationContext(),add_book_library.class);
        i.putExtra("dept_name",dept_name);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"EEE",Toast.LENGTH_SHORT).show();
    }

    public void library_mech_btn(View view) {

        dept_name = "mech";
        Intent i = new Intent(getApplicationContext(),add_book_library.class);
        i.putExtra("dept_name",dept_name);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"MECH",Toast.LENGTH_SHORT).show();
    }

    public void library_civil_btn(View view) {

        dept_name = "civil";
        Intent i = new Intent(getApplicationContext(),add_book_library.class);
        i.putExtra("dept_name",dept_name);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"CIVIL",Toast.LENGTH_SHORT).show();
    }

    public void library_mct_btn(View view) {

        dept_name = "mct";
        Intent i = new Intent(getApplicationContext(),add_book_library.class);
        i.putExtra("dept_name",dept_name);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"MCT",Toast.LENGTH_SHORT).show();
    }

    public void library_bio_btn(View view) {

        dept_name = "bio";
        Intent i = new Intent(getApplicationContext(),add_book_library.class);
        i.putExtra("dept_name",dept_name);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"Bio-Tech",Toast.LENGTH_SHORT).show();
    }

    public void library_nano_btn(View view) {

        dept_name = "nano";
        Intent i = new Intent(getApplicationContext(),add_book_library.class);
        i.putExtra("dept_name",dept_name);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"Nano-Tech",Toast.LENGTH_SHORT).show();
    }

    public void library_ei_btn(View view) {

        dept_name = "ei";
        Intent i = new Intent(getApplicationContext(),add_book_library.class);
        i.putExtra("dept_name",dept_name);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"E & I",Toast.LENGTH_SHORT).show();
    }

    public void library_food_btn(View view) {

        dept_name = "food";
        Intent i = new Intent(getApplicationContext(),add_book_library.class);
        i.putExtra("dept_name",dept_name);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"Food-Tech",Toast.LENGTH_SHORT).show();
    }

    public void library_textile_btn(View view) {

        dept_name = "textile";
        Intent i = new Intent(getApplicationContext(),add_book_library.class);
        i.putExtra("dept_name",dept_name);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"Textile",Toast.LENGTH_SHORT).show();
    }

    public void library_it_btn(View view) {

        dept_name = "it";
        Intent i = new Intent(getApplicationContext(),add_book_library.class);
        i.putExtra("dept_name",dept_name);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"IT",Toast.LENGTH_SHORT).show();
    }


    class GestureClass extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


            if(true && HomepageFlag == 1) {
                if (e2.getX() < e1.getX()) {

                    if (position < 4) {
                        incrementTabPosition();
                        swipeChange();
                    }

                } else if (e2.getX() > e1.getX()) {


                    if (position > 1) {
                        decrementTabPosition();
                        swipeChange();
                    }
                }
            }

            return true;
        }
    }

    public void swipeChange()
    {
        if(position==1)
        {
            timeline.setImageResource(R.mipmap.ic_timeline_red);
            menu.setImageResource(R.mipmap.ic_menu_green);
            group.setImageResource(R.mipmap.ic_group_green);
            notification.setImageResource(R.mipmap.ic_notification_green);
            menuSideView.setVisibility(View.INVISIBLE);
            menuButton.setVisibility(View.VISIBLE);
            menuView.setVisibility(View.INVISIBLE);
            if(types.equals("staff")) {
                addButtonInTimeline.setVisibility(View.VISIBLE);
            }
            menuSideFlag = false;
            tabChanger.setDisplayedChild(0);

        }
        if(position==2)
        {

            timeline.setImageResource(R.mipmap.ic_timeline_green);
            menu.setImageResource(R.mipmap.ic_menu_green);
            group.setImageResource(R.mipmap.ic_group_red);
            notification.setImageResource(R.mipmap.ic_notification_green);
            menuSideView.setVisibility(View.INVISIBLE);
            menuButton.setVisibility(View.VISIBLE);
            menuView.setVisibility(View.INVISIBLE);
            menuSideFlag = false;
            tabChanger.setDisplayedChild(1);
        }
        if(position==3)
        {
            timeline.setImageResource(R.mipmap.ic_timeline_green);
            menu.setImageResource(R.mipmap.ic_menu_green);
            group.setImageResource(R.mipmap.ic_group_green);
            notification.setImageResource(R.mipmap.ic_notification_red);
            menuSideView.setVisibility(View.INVISIBLE);
            menuView.setVisibility(View.INVISIBLE);
            menuSideFlag = false;
            menuButton.setVisibility(View.VISIBLE);
            tabChanger.setDisplayedChild(2);
        }
        if(position==4)
        {
            timeline.setImageResource(R.mipmap.ic_timeline_green);
            menu.setImageResource(R.mipmap.ic_menu_red);
            group.setImageResource(R.mipmap.ic_group_green);
            notification.setImageResource(R.mipmap.ic_notification_green);
            menuSideView.setVisibility(View.VISIBLE);
            menuButton.setVisibility(View.INVISIBLE);
            menuView.setVisibility(View.INVISIBLE);
            menuSideFlag = true;
            tabChanger.setDisplayedChild(3);
        }
    }

    boolean backPressFlag = false;
    @Override
    public void onBackPressed() {

        if(homepageFlag||!loggedInFlag) {
            if (backPressFlag) {
                finish();
            } else {
                backPressFlag = true;
                Toast.makeText(getApplicationContext(), "Press again to exit..", Toast.LENGTH_SHORT).show();

                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onFinish() {
                        backPressFlag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                }.start();
            }
        }
        else if(!homepageFlag)
        {
            layoutChanger.setDisplayedChild(2);
            menuButton.setVisibility(View.VISIBLE);
            homepageFlag = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            addImageOrVideo.setImageURI(imageUri);
            Bitmap bitmap = ((BitmapDrawable)addImageOrVideo.getDrawable()).getBitmap();
            addImageOrVideo.setImageBitmap(bitmap);
           // performCrop();
        }

       /* if(requestCode == PIC_CROP && resultCode == RESULT_OK)
        {
            Bundle extra = data.getExtras();
            pic = extra.getParcelable("data");
            addImageOrVideo.setImageBitmap(pic);
        }*/
    }

    public void timelineStart()
    {
        FirebaseRecyclerAdapter<CardDetails,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CardDetails, BlogViewHolder>(
                CardDetails.class,
                R.layout.card,
                BlogViewHolder.class,
                titleAndDesc
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, CardDetails model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());
            }

        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
// Set the layout manager to your recyclerview
        mRecyclerView.setLayoutManager(mLayoutManager);


    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title)
        {
            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc(String desc)
        {
            TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image)
        {
            ImageView post_image = (ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }

    public void setProfile()
    {
        Firebase acc = new Firebase("https://sacw-signup.firebaseio.com/KSRCOLLEGEOFTECHNOLOGY/"+department+"/"+batchYear+"/"+registerNumber+"/");

        acc.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,String> map = dataSnapshot.getValue(Map.class);
                profileName.setText("Name   :  "+map.get("name"));
                profileReg.setText("Register Number :  "+registerNumber);
                profileAddress.setText("Address :  "+map.get("address"));
                profileBatch.setText("Batch :  "+map.get("batch"));
                profileEmail.setText("Email :  "+map.get("email"));
                profileFatherName.setText("Father Name  :  "+map.get("fathername"));
                profileFatherPhone.setText("Father Phone :  "+map.get("fatherphone"));
                profileMotherName.setText("Mother Name  :  "+map.get("mothername"));
                profileMotherPhone.setText("Mother Phone :  "+map.get("motherphone"));
                profileGender.setText("Gender   :  "+map.get("gender"));
                profilePhone.setText("Phone :  "+map.get("phone"));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void group_chat() {

        final ArrayList group_array_list = new ArrayList();
        group_array_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,group_array_list);
        group_list.setAdapter(group_array_adapter);

        Group_list = new Firebase("https://sacw-signup.firebaseio.com/KSRCOLLEGEOFTECHNOLOGY/"+department+"/"+batchYear+"/"+registerNumber+"/group");

        Group_list.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String group_name = dataSnapshot.getValue(String.class);
                group_array_list.add(group_name);
                group_array_adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    //marshmalloW permission

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_LOCATION);


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
        }
    }



}