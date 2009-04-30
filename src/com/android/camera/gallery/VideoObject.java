/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.camera.gallery;

import com.android.camera.ImageManager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a particular video and provides access to the underlying data and
 * two thumbnail bitmaps as well as other information such as the id, and the
 * path to the actual video data.
 */
public class VideoObject extends BaseImage implements IImage {

    /**
     * Constructor.
     *
     * @param id        the image id of the image
     * @param cr        the content resolver
     */
    protected VideoObject(long id, long miniThumbMagic, ContentResolver cr,
            VideoList container, int row) {
        super(id, miniThumbMagic, cr, container, row);
    }

    @Override
    protected Bitmap.CompressFormat compressionType() {
        return Bitmap.CompressFormat.JPEG;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof VideoObject)) return false;
        return fullSizeImageUri().equals(
                ((VideoObject) other).fullSizeImageUri());
    }

    @Override
    public int hashCode() {
        return fullSizeImageUri().toString().hashCode();
    }

    public String getDataPath() {
        String path = null;
        Cursor c = getCursor();
        synchronized (c) {
            if (c.moveToPosition(getRow())) {
                int column = ((VideoList) getContainer()).indexData();
                if (column >= 0) path = c.getString(column);
            }
        }
        return path;
    }

    @Override
    public Bitmap fullSizeBitmap(int targetWidthHeight) {
        return ImageManager.NO_IMAGE_BITMAP;
    }

    @Override
    public ICancelable<Bitmap> fullSizeBitmapCancelable(
            int targetWidthHeight) {
        return null;
    }

    @Override
    public InputStream fullSizeImageData() {
        try {
            InputStream input = mContentResolver.openInputStream(
                    fullSizeImageUri());
            return input;
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public long fullSizeImageId() {
        return mId;
    }

    @Override
    public int getHeight() {
         return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    public boolean isReadonly() {
        return false;
    }

    public boolean isDrm() {
        return false;
    }

    public boolean rotateImageBy(int degrees) {
       return false;
    }

    public Bitmap thumbBitmap() {
        return fullSizeBitmap(THUMBNAIL_TARGET_SIZE);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("" + mId);
        return sb.toString();
    }
}