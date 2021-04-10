package org.asamk.signal.manager.storage.groups;

import org.asamk.signal.manager.groups.GroupId;
import org.asamk.signal.manager.groups.GroupIdV1;

import java.util.List;

public interface GroupStore {
    void updateGroup(GroupInfo group);

    void deleteGroup(GroupId groupId);

    GroupInfo getGroup(GroupId groupId);

    GroupInfoV1 getOrCreateGroupV1(GroupIdV1 groupId);

    List<GroupInfo> getGroups();
}
