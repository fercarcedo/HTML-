import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Simple HTML preprocessor
 * Receives as an input the files with .htmlpp extension
 * and creates an equivalent .html file for each one
 * 
 * @author fercarcedo
 *
 */
public class HTMLPP {
	private static final Map<String, String> variablesMap = new HashMap<>();

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.err.println("Usage: java HTMLPP files");
			return;
		}

		for(String filename : args)
			handleFile(filename);
	}
	
	private static void handleFile(String filename) throws IOException {
		BufferedReader bfReader = new BufferedReader(new FileReader(filename));
		int indexExt = filename.lastIndexOf(".");
		BufferedWriter bfWriter = new BufferedWriter(new FileWriter(filename.substring(0, indexExt) + ".html"));

		String prevToken = null;
		String variableName = null;
		String variableContent = "";
		boolean insideVariable = false;

		while (bfReader.ready()) {
			String line = bfReader.readLine();
			StringTokenizer st = new StringTokenizer(line);

			while (st.hasMoreTokens()) {
				String token = st.nextToken();

				if (token.startsWith("src=") && "<include".equals(prevToken)) {
					// Copy included file to dest
					int start = token.indexOf("\"");
					int end = token.lastIndexOf("\"");
					if (start == -1) {
						start = token.indexOf("'");
						end = token.lastIndexOf("'");
					}

					BufferedReader includedReader = new BufferedReader(new FileReader(token.substring(start + 1, end)));

					while (includedReader.ready()) {
						bfWriter.write(includedReader.readLine());
						bfWriter.newLine();
					}

					includedReader.close();
				} else if (token.startsWith("name=") && "<variable".equals(prevToken)) {
					int start = token.indexOf("\"");
					int end = token.lastIndexOf("\"");
					if (start == -1) {
						start = token.indexOf("'");
						end = token.lastIndexOf("'");
					}

					variableName = token.substring(start + 1, end);
					insideVariable = true;
				} else if (("</variable>").equals(token)) {
					variablesMap.put(variableName, variableContent);
					insideVariable = false;
				} else {
					int startVarRef = token.indexOf("${") + 2;
					int endVarRef = token.lastIndexOf("}");

					if (startVarRef != 1) {// -1 + 2
						String varName = token.substring(startVarRef, endVarRef);
						token = token.replace("${" + varName + "}", 
								variablesMap.get(varName));
					}

					// Simply copy to dest file
					if (!token.equals("/>") && !token.equals("<variable")
							&& !insideVariable
							&& !token.equals("<include")) {
						if (token.endsWith(">"))
							bfWriter.write(token + System.lineSeparator());
						else
							bfWriter.write(token + " ");
					}
				}

				if (insideVariable && !(token.startsWith("name=") && "<variable".equals(prevToken)))
					variableContent += token;

				prevToken = token;
			}
		}

		bfReader.close();
		bfWriter.close();
	}
}
