package hcay.pui.com.recognizer;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.google.gson.*;

public class TemplateManager {

	private static String templateFilePath;
	private static final Gson GSON = new Gson();

	public static ArrayList<Template> templates = new ArrayList<>();

	private static boolean changed = false;

	private static AssetManager assetManager;

	public static void initialize(Context context) {
//		templateFilePath
		//.open("templates.g");
		assetManager = context.getAssets();
		templates = load();
	}

	public static ArrayList<Template> load() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open("templates.g")));

			Type listType = new TypeToken<ArrayList<Template>>(){}.getType();
			ArrayList<Template> t = GSON.fromJson(reader, listType);

			for (Template template : t) {
				if (!template.normalized) {
					template.normalizePoints();
					changed = true;
				}
			}

			if (changed) saveTemplates(t);
			return t;
		} catch (IOException e) {
			return new ArrayList<>();
		}
	}

	public static void addNewTemplate(Gesture gesture, ArrayList<Point> points) {
		templates.add(new Template(gesture, points));
		changed = true;
	}

	public static void addNewTemplate(Template template) {
		templates.add(template);
		changed = true;
	}

	public static void save() {
		if (changed) {
			saveTemplates(templates);
		}
	}

	private static void saveTemplates(ArrayList<Template> templates) {
		try {
			FileWriter writer = new FileWriter(templateFilePath);
			// Gson gson = new GsonBuilder().setPrettyPrinting().create();
			GSON.toJson(templates, writer);
			writer.close();
		} catch (IOException e) {}
	}

}