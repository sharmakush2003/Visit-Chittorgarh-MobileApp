package com.example.visitchittorgarh.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object BookingManager {
    private val db = FirebaseFirestore.getInstance()
    private val _passes = MutableStateFlow<List<TravelPass>>(emptyList())
    val passes: StateFlow<List<TravelPass>> = _passes

    private var snapshotListenerRegistration: ListenerRegistration? = null

    fun listenToUserPasses(userId: String) {
        snapshotListenerRegistration?.remove()
        snapshotListenerRegistration = db.collection("passes")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(TravelPass::class.java) }
                    _passes.value = list
                }
            }
    }

    fun stopListening() {
        snapshotListenerRegistration?.remove()
        snapshotListenerRegistration = null
        _passes.value = emptyList()
    }

    fun addPass(pass: TravelPass, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        db.collection("passes").document(pass.passCode).set(pass)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getPass(passCode: String, onResult: (TravelPass?) -> Unit) {
        db.collection("passes").document(passCode).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onResult(document.toObject(TravelPass::class.java))
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun updatePass(updatedPass: TravelPass, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        db.collection("passes").document(updatedPass.passCode).set(updatedPass)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
