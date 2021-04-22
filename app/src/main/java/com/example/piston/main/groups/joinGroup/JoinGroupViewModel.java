package com.example.piston.main.groups.joinGroup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

public class JoinGroupViewModel extends ViewModel implements JoinGroupRepository.IJoinGroup {

    private final MutableLiveData<String> groupCodeField = new MutableLiveData<>("");
    private final MutableLiveData<JoinGroupResult.JoinError> groupCodeFieldError =
            new MutableLiveData<>(JoinGroupResult.JoinError.NONE);
    private final MutableLiveData<Boolean> createGroup = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> joinedGroup = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    private final JoinGroupRepository repository = new JoinGroupRepository(this);

    public void joinGroup() {
        loading.setValue(true);
        repository.joinGroup(Objects.requireNonNull(groupCodeField.getValue()));
    }

    public void groupCodeUpdate() {
        repository.checkGroupCode(Objects.requireNonNull(groupCodeField.getValue()));
    }

    public void setCreateGroup() {
        createGroup.setValue(true);
    }

    @Override
    public void setGroupCodeError(JoinGroupResult.JoinError error) {
        groupCodeFieldError.setValue(error);
    }

    @Override
    public void setLoadingFinished() {
        loading.setValue(false);
    }

    @Override
    public void setJoinGroupFinished() {
        joinedGroup.setValue(true);
    }

    public MutableLiveData<String> getGroupCodeField() {
        return groupCodeField;
    }

    public LiveData<Boolean> getCreateGroup() {
        return createGroup;
    }

    public LiveData<Boolean> getJoinedGroup() {
        return joinedGroup;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<JoinGroupResult.JoinError> getGroupCodeFieldError() {
        return groupCodeFieldError;
    }
}
