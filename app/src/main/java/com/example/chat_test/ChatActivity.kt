package com.example.chat_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_test.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    // 받는사람 이름, uid
    private lateinit var receiverName: String
    private lateinit var receiverUid: String

    //바인딩 객체
    private lateinit var binding: ActivityChatBinding

    lateinit var mAuth: FirebaseAuth // 파이어 베이스의 사용자 인증 및 다양한 작업을 위한 객체
    lateinit var mDbRef: DatabaseReference// 파이어 베이스에서 데이터를 읽거나 쓰기 위한 객체

    private lateinit var receiverRoom: String // 받는 대화방
    private lateinit var senderRoom: String // 보낸 대화방

    private lateinit var messageList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기화
        messageList = ArrayList()
        val messageAdapter: MessageAdapter = MessageAdapter(this, messageList)

        // RecyclerView 설정
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        // messageAdapter 에서 받아오는 messageList를 RecyclerView에다가 뿌림.
        binding.chatRecyclerView.adapter = messageAdapter

        // 넘어온 데이터 변수에 담기
        receiverName = intent.getStringExtra("name").toString()
        receiverUid = intent.getStringExtra("uId").toString()

        // 인증 객체 초기화
        mAuth = FirebaseAuth.getInstance()

        // Firebase 데이터를 받을 객체를 초기화합니다.
        mDbRef = FirebaseDatabase.getInstance().reference

        // senderUid에 보내는 사람 uid 담기
        val senderUid = mAuth.currentUser?.uid

        // 보내는데이터 담을 uid 생성(받는사람 uid+보내는 사람 uid)
        senderRoom = receiverUid + senderUid

        // 받는데이터 담을 uid 생성(보내는 사람 uid+받는사람 uid)
        receiverRoom = senderUid + receiverUid

        // 액션바title에 상대방 이름 보여주기
        supportActionBar?.title = receiverName

        // 메시지 전송 버튼 이벤트
        binding.sendBtn.setOnClickListener {
            // 메시지 내용 가져오기
            val message = binding.messageEdit.text.toString()
            // Message 객체 생성
            val messageObject = Message(message, senderUid)

            // push().setValue(messageObject) 보낸 대화방에 데이터 저장
            mDbRef.child("chats").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    // addOnSuccessListener 저장 성공하면 받는 대화방에 데이터 저장
                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                        .setValue(messageObject)
                }
            //채팅입력창 ""로 초기화
            binding.messageEdit.setText("")
        }

        //메시지 가져오기
        mDbRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object: ValueEventListener {
                // ValueEventListener 리스너의 이벤트 콜백 onDataChange():
                // 이벤트 발생 시점에 특정 경로에 있던 콘텐츠의 정적 스냅샷을 읽고 변경사항을 수신 대기합니다.
                override fun onDataChange(snapshot: DataSnapshot) {
                    // 이전 메시지 삭제
                    messageList.clear()
                    Log.d("lsy", mDbRef.child("chats").child(senderRoom).child("messages").toString())

                    //데이터베이스에서 받아온 메시지들을 messageList에 추가
                    for(postSnapshat in snapshot.children){

                        // 메세지 정보를 Message 객체로 가져와 message 변수에 저장
                        val message = postSnapshat.getValue(Message::class.java)
                        // message변수에 저장된 정보들 messageList에 추가
                        messageList.add(message!!)
                    }
                    Log.d("lsy", "메세지확인 : $messageList")
                    // 변경된 messageList 적용
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}