package com.example.firebasetask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore objectFirebaseFirestore;
    private CollectionReference ObjectCollectioReference;
    private DocumentReference objectDocumentReference;
    private Dialog objectDialog;
    private EditText  stdId,stdname,stdrollno;
    private TextView tv_one;
    private static final String CollectionName="BSCS6A";
    private static final String Student_ID="";
    private static final String Student_Name="student_name";
    private static final String Student_Rollno="student_rollno";
    private String allData ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objectDialog=new Dialog(this);
        objectDialog.setContentView(R.layout.please_wait);

        stdId=findViewById(R.id.stdIDET);
        stdname=findViewById(R.id.stdNameET);
        stdrollno=findViewById(R.id.stdrollnoET);

        tv_one = findViewById(R.id.tv_one);

        try {
            objectFirebaseFirestore=FirebaseFirestore.getInstance();
             ObjectCollectioReference=objectFirebaseFirestore.collection(CollectionName);
        }
        catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    public void  showdata(View v)
    {

        try
        {
                objectDialog.show();
                ObjectCollectioReference.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                objectDialog.dismiss();
                                tv_one.setText("");
                                stdId.setText("");
                                for (DocumentSnapshot objectDocumentReference : queryDocumentSnapshots) {
                                    String std_ID = objectDocumentReference.getId();
                                    String std_Name = objectDocumentReference.getString(Student_Name);
                                    String std_rollno = objectDocumentReference.getString(Student_Rollno);
                                    allData += "Student ID : " + std_ID + '\n' + "Student Name : " + std_Name + '\n' + "Student Rollno : " + std_rollno ;
                                }
                                tv_one.setText(allData);
                                Toast.makeText(MainActivity.this,"Retrieve Data Succcessf",Toast.LENGTH_LONG).show();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        objectDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Fails to retrieve data:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        }
        catch(Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void  deletecollection(View v)
    {
        try
        {
            if(!stdId.getText().toString().isEmpty())
            {


                objectDocumentReference = objectFirebaseFirestore.collection(CollectionName)
                .document(stdId.getText().toString());



                objectDocumentReference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Do id Deleted",Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"fails to delete",Toast.LENGTH_LONG).show();
                            }
                        });
            }
            else
            {
                Toast.makeText(this,"Fails to delete the Document",Toast.LENGTH_LONG);
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    public void addvalues(View v) {

        try {
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
            objectFirebaseFirestore.collection(CollectionName).document(stdId.getText().toString()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                            if (task.getResult().exists()) {
                                Toast.makeText(MainActivity.this, "You Have Already Created", Toast.LENGTH_SHORT).show();
                            }

                            else
                            {
                                if(!stdId.getText().toString().isEmpty() && !stdname.getText().toString().isEmpty() && !stdrollno.getText().toString().isEmpty()) {
                                    objectDialog.show();

                                    Map<String,Object> objMap=new HashMap<>();
                                    objMap.put(Student_Name, stdname.getText().toString());
                                    objMap.put(Student_Rollno, stdrollno.getText().toString());
                                    objectFirebaseFirestore.collection(CollectionName)
                                            .document(stdId.getText().toString()).set(objMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    objectDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    objectDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "Fails to add data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });

        }
        catch (Exception e)
        {

            Toast.makeText(this, "Add Values"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }
}
