package com.marija.diplomski.places.core.domain.data;

import com.marija.diplomski.places.core.domain.model.Folder;

import java.util.List;

public interface FolderDao {

    List<Folder> getAllFolders();
    Folder addFolder(Folder folder);
    boolean deleteFolders(List<Integer> folderIds);
    boolean updateFolder(Folder folder);
}
