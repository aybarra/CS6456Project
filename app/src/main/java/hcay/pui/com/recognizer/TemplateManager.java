package hcay.pui.com.recognizer;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import java.io.IOException;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.google.gson.*;

public class TemplateManager {

	private static final String TEMPLATE_FILE_NAME = "templates.g";
	private static final Gson GSON = new Gson();

	public static ArrayList<Template> templates = new ArrayList<>();

	private static boolean changed = false;

	public static void initialize(Context context) {
		templates = load(context);
	}

	public static ArrayList<Template> load(Context context) {
        BufferedReader reader;
		try {
            reader = new BufferedReader(new InputStreamReader(context.openFileInput(TEMPLATE_FILE_NAME)));
            changed = true;
        } catch (FileNotFoundException e) {
            try {
                reader = new BufferedReader(new InputStreamReader(context.getAssets().open(TEMPLATE_FILE_NAME)));
            } catch (IOException e1) {
                return new ArrayList<>();
            }
        }

        Type listType = new TypeToken<ArrayList<Template>>() {
        }.getType();
        ArrayList<Template> t = GSON.fromJson(reader, listType);

        for (Template template : t) {
            if (!template.normalized) {
                template.normalizePoints();
                changed = true;
            }
        }

        if (changed) saveTemplates(t, context);
        return t;
	}

	public static boolean resetTemplates(Context context) {
		boolean deleted = context.deleteFile(TEMPLATE_FILE_NAME);
        templates = load(context);
        return deleted && !templates.isEmpty();
	}

	public static void addNewTemplate(Gesture gesture, ArrayList<Point> points) {
		templates.add(new Template(gesture, points));
		changed = true;
	}

	public static void addNewTemplate(Template template) {
		templates.add(template);
		changed = true;
	}

	public static void save(Context context) {
		if (changed) {
			saveTemplates(templates, context);
		}
	}

	private static void saveTemplates(ArrayList<Template> templates, Context context) {
		try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(TEMPLATE_FILE_NAME, Context.MODE_PRIVATE)));
			GSON.toJson(templates, writer);
			writer.close();
		} catch (IOException e) {}
	}

}