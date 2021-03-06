
package novoda.lib.sqliteprovider.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;

public class UriUtils {

	private static final String TAG = UriUtils.class.getSimpleName();

	private Uri wrapped = null;

	private boolean isItem = false;

	private static Map<String, String> mappedIds = new HashMap<String, String>();

	public static UriUtils from(Uri uri) {
		UriUtils utils = new UriUtils();
		utils.wrapped = uri;

		final List<String> segs = uri.getPathSegments();
		String parent = "";

		for (int i=0;i<segs.size();i++){
			String currentSeg = segs.get(i);
			if(isNumeric(currentSeg)){
				final int children = Integer.parseInt(currentSeg);
				for(int l=0; l<children; l++){
					mappedIds.put(parent, currentSeg);
				}
			}else{
				parent = currentSeg;
			}
		}
		return utils;
	}

	public static boolean isNumeric(String NumericChar){
		try{
			Integer.parseInt(NumericChar);
		} catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static boolean isNumberedEntryWithinCollection(final Uri uri) {
		return isNumeric(uri.getLastPathSegment());
	}

	public static boolean isDir(final Uri uri) {
		return isDir("", uri);
	}

	public static boolean isDir(final String rootPath, final Uri uri) {
		return !isItem(rootPath, uri);
	}

	public static boolean isItem(final Uri uri) {
		return isItem("", uri);
	}

	public static boolean isItem(final String rootPath, final Uri uri) {
		final List<String> segments = uri.getPathSegments();
		if (rootPath != null && !rootPath.equals("")) {
			return (((segments.size() - rootPath.split("/").length + 1) % 2) == 1);
		} else {
			return ((segments.size() % 2) == 0);
		}
	}

	public static String getItemDirID(final Uri uri) {
		return getItemDirID("", uri);
	}

	public static String getItemDirID(final String rootPath, final Uri uri) {
		final List<String> segments = uri.getPathSegments();
		if (isItem(rootPath, uri)) {
			return segments.get(segments.size() - 2);
		} else {
			return uri.getLastPathSegment();
		}
	}

	/*	public static boolean hasParent(Uri uri){
			if (!isNumberedEntryWithinCollection(uri)){
				if (uri.getPathSegments().size()>2){
					return true;
				}  else if (uri.getPathSegments().size()>3){
					return true;
				}
			}
		return false;
	}*/

	public static boolean hasParent(Uri uri){
		if (uri.getPathSegments().size()>2){
			return true;
		}
		return false;
	}



	public static String getParentColumnName(Uri uri){
		if (hasParent(uri)){
			if (!isNumberedEntryWithinCollection(uri)){
				return uri.getPathSegments().get((uri.getPathSegments().size()-1)-2);
			} else {				
				return uri.getPathSegments().get((uri.getPathSegments().size()-1)-3);
			}				
		}
		return "";
	}

	public static String getParentId(Uri uri){
		if (hasParent(uri)){
			if (!isNumberedEntryWithinCollection(uri)){
				return uri.getPathSegments().get((uri.getPathSegments().size()-1)-1);
			} else {
				return uri.getPathSegments().get((uri.getPathSegments().size()-1)-2);
			}			
		}
		return "";
	}


	public Map<String, String> getMappedIds() {
		return mappedIds;
	}

}
