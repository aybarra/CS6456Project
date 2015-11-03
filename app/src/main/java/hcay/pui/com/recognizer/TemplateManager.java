package hcay.pui.com.recognizer;

import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.google.gson.*;

public class TemplateManager {

	private static final String TEMPLATE_FILE_PATH = "templates.g";
	private static final Gson GSON = new Gson();

	public static ArrayList<Template> templates = load();

	private static boolean changed = false;

	public static ArrayList<Template> load() {
		try {
			FileReader reader = new FileReader(TEMPLATE_FILE_PATH);
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
		} catch (FileNotFoundException e) {
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
			FileWriter writer = new FileWriter(TEMPLATE_FILE_PATH);
			// Gson gson = new GsonBuilder().setPrettyPrinting().create();
			GSON.toJson(templates, writer);
			writer.close();
		} catch (IOException e) {}
	}

}