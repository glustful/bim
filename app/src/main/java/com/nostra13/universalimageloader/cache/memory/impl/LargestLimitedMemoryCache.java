/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.LimitedMemoryCache;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import uk.co.senab.photoview.BitmapResult;

/**
 * Limited {@link Bitmap bitmap} cache. Provides {@link Bitmap bitmaps} storing. Size of all stored bitmaps will not to
 * exceed size limit. When cache reaches limit size then the bitmap which has the largest size is deleted from
 * cache.<br />
 * <br />
 * <b>NOTE:</b> This cache uses strong and weak references for stored Bitmaps. Strong references - for limited count of
 * Bitmaps (depends on cache size), weak references - for all other cached Bitmaps.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public class LargestLimitedMemoryCache extends LimitedMemoryCache {
	/**
	 * Contains strong references to stored objects (keys) and sizes of the objects. If hard cache
	 * size will exceed limit then object with the largest size is deleted (but it continue exist at
	 * {@link #softMap} and can be collected by GC at any time)
	 */
	private final Map<BitmapResult, Integer> valueSizes = Collections.synchronizedMap(new HashMap<BitmapResult, Integer>());

	public LargestLimitedMemoryCache(int sizeLimit) {
		super(sizeLimit);
	}

	@Override
	public boolean put(String key, BitmapResult value) {
		if (super.put(key, value)) {
			valueSizes.put(value, getSize(value));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public BitmapResult remove(String key) {
		BitmapResult value = super.get(key);
		if (value != null) {
			valueSizes.remove(value);
		}
		return super.remove(key);
	}

	@Override
	public void clear() {
		valueSizes.clear();
		super.clear();
	}

	@Override
	protected int getSize(BitmapResult value) {
		return value.getBitmap().getRowBytes() * value.getBitmap().getHeight();
	}

	@Override
	protected BitmapResult removeNext() {
		Integer maxSize = null;
		BitmapResult largestValue = null;
		Set<Entry<BitmapResult, Integer>> entries = valueSizes.entrySet();
		synchronized (valueSizes) {
			for (Entry<BitmapResult, Integer> entry : entries) {
				if (largestValue == null) {
					largestValue = entry.getKey();
					maxSize = entry.getValue();
				} else {
					Integer size = entry.getValue();
					if (size > maxSize) {
						maxSize = size;
						largestValue = entry.getKey();
					}
				}
			}
		}
		valueSizes.remove(largestValue);
		return largestValue;
	}

	@Override
	protected Reference<BitmapResult> createReference(BitmapResult value) {
		return new WeakReference<BitmapResult>(value);
	}
}
