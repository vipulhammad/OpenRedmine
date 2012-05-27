package jp.redmine.redmineclient.external;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import jp.redmine.redmineclient.entity.RedmineProject;
import jp.redmine.redmineclient.entity.TypeConverter;

public class ProjectParser extends BaseParser<RedmineProject> {
	//private final String TAG = this.toString();

	@Override
	public void parse() throws XmlPullParserException, IOException {
		if (xml == null){
			Log.e("ProjectParser", "xml is null");
			return;
		}
		int eventType = xml.getEventType();
		RedmineProject item = null;
		Log.d("ProjectParser","start parse");
		while (eventType != XmlPullParser.END_DOCUMENT) {
			eventType = xml.next();
			switch (eventType){
			case XmlPullParser.START_DOCUMENT:
				Log.d("ProjectParser","START_DOCUMENT");
				break;
			case XmlPullParser.START_TAG:
				Log.d("ProjectParser","START_TAG ".concat(xml.getName()));
				if("project".equalsIgnoreCase(xml.getName())){
					item = new RedmineProject();
				} else if(item != null){
					parseInternal(item);
				}
				break;
			case XmlPullParser.END_TAG:
				Log.d("ProjectParser","END_TAG ".concat(xml.getName()));
				if("project".equalsIgnoreCase(xml.getName())){
					notifyDataCreation(item);
					item = null;
				}
				break;
			case XmlPullParser.TEXT:
				Log.d("ProjectParser","TEXT ".concat(xml.getText()));
				break;
			}
		}

	}

	private void parseInternal(RedmineProject item) throws XmlPullParserException, IOException{
		if(xml.getDepth() <= 2)
			return;
		if("id".equalsIgnoreCase(xml.getName())){
			String work = getNextText();
			if("".equals(work))	return;
			item.ProjectId(Integer.parseInt(work));
		} else if("name".equalsIgnoreCase(xml.getName())){
			item.Name(getNextText());
		} else if("identifier".equalsIgnoreCase(xml.getName())){
			item.Identifier(getNextText());
		} else if("description".equalsIgnoreCase(xml.getName())){
			item.Description(getNextText());
		} else if("homepage".equalsIgnoreCase(xml.getName())){
			item.Homepage(getNextText());
		} else if("created_on".equalsIgnoreCase(xml.getName())){
			item.Created(TypeConverter.ParseDate(getNextText()));
		} else if("updated_on".equalsIgnoreCase(xml.getName())){
			item.Modified(TypeConverter.ParseDate(getNextText()));
		}
		//@todo tracker, issue_categories

	}
}