package com.example.chat_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_test.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: UserAdapter

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    private lateinit var userList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //인증 초기화
        mAuth = Firebase.auth

        // Firebase 데이터를 받을 객체를 초기화합니다.
        mDbRef = Firebase.database.reference

        // 사용자 정보를 저장할 ArrayList<User> 객체를 초기화합니다.
        userList = ArrayList()

        // UserAdapter 객체를 초기화합니다.
        adapter = UserAdapter(this, userList)

        // RecyclerView의 레이아웃 매니저와 어댑터를 설정합니다.
        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.userRecyclerView.adapter = adapter

        // "user" 노드에서 addValueEventListener를 사용하여 사용자 정보를 가져옵니다.
        // ValueEventListener 리스너의 이벤트 콜백 onDataChange(): 이벤트 발생 시점에 특정 경로(mDbRef.child("user"))에 있던 콘텐츠의 정적 스냅샷을 읽고 변경사항을 수신 대기합니다.
        mDbRef.child("user").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // 데이터 스냅샷의 모든 하위 노드를 반복합니다.
                for(postSnapshot in snapshot.children){
                // 현재 유저 정보를 User 객체로 가져와 currentUser 변수에 저장
                val currentUser = postSnapshot.getValue(User::class.java)
                // for 루프를 통해 하위 요소를 하나씩 가져와서 User 객체로 변환합니다
                if(mAuth.currentUser?.uid != currentUser?.uId){
                    // userList에 유저 정보를 추가합니다.
                    userList.add(currentUser!!)
                    }
                }
                // 어댑터에 변경 사항이 있음을 알립니다.
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //실패 시 실행
            }
        })
    }//onCreate

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 메뉴를 불러옵니다.
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // "log_out" 메뉴 아이템이 선택된 경우 로그아웃합니다.
        if(item.itemId == R.id.log_out){
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return true
    }
}