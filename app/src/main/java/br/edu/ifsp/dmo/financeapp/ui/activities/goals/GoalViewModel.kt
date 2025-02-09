package br.edu.ifsp.dmo.financeapp.ui.activities.goals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.dmo.financeapp.data.entity.goal.Goal
import br.edu.ifsp.dmo.financeapp.data.repository.goal.GoalRepository
import kotlinx.coroutines.launch

class GoalViewModel(application: Application) : AndroidViewModel(application) {

    private val goalRepository = GoalRepository(application)

    private val _goals = MutableLiveData<List<Goal>>()
    val goals: LiveData<List<Goal>> = _goals

    private var emailUser: String = ""

    private val _inserted = MutableLiveData<Boolean>()
    val inserted: LiveData<Boolean> = _inserted

    private val _deleted = MutableLiveData<Boolean>()
    val deleted: LiveData<Boolean> = _deleted

    private val _updated = MutableLiveData<Boolean>()
    val updated: LiveData<Boolean> = _updated

    fun insertGoal(name: String, accumulatedValue: Double, targetValue: Double){
        viewModelScope.launch {
            goalRepository.create(Goal(name = name, accumulated_value = accumulatedValue, target_value = targetValue, email = emailUser))
            _inserted.value = true
            load()
        }
    }

    fun removeGoal(id: Long){
        viewModelScope.launch {
            val goal = goalRepository.getGoalById(id)

            if(goal != null){
                goalRepository.delete(goal)
                _deleted.value = true
                load()
            }
        }
    }

    fun updateGoal(id: Long, amount: Double){
        viewModelScope.launch {
            val goal = goalRepository.getGoalById(id)

            if(goal != null){
                goalRepository.updateAccumulated(goal, amount)
                _updated.value = true
                load()
            }
        }
    }

    fun setEmail(email: String){
        emailUser = email
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _goals.value = goalRepository.getAllByEmail(emailUser)
        }
    }
}