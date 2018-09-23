package exercise.vigenere;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
	
	//Default Character Set
	public static final String CIPHER_CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static Map<Character, Integer> posMap = new HashMap<>();

	public static void main(String args[]) {

		if (args.length != 3) {
			System.out.println("Exact 3 parameters required - [action] [key] [target]");
			System.exit(1);
		}

		for (int i = 0; i < CIPHER_CHAR_SET.length(); i++) {
			posMap.put(CIPHER_CHAR_SET.charAt(i), i);
		}

		String action, key, target;
		action = args[0];
		key = args[1];
		target = args[2];

		if ("encrypt".equalsIgnoreCase(action)) {
			System.out.println(encrypt(target, key));
		} else if ("decrypt".equalsIgnoreCase(action)) {
			System.out.println(decrypt(target, key));
		} else if ("encryptDir".equalsIgnoreCase(action)) {
			encryptDir(target, key);
		} else if ("decryptDir".equalsIgnoreCase(action)) {
			decryptDir(target, key);
		} else {
			System.out.println("action [" + action + "] not implemented");
		}

	}
	
	//Method to encrypt input string and return encrypted string
	public static String encrypt(String target, final String key) {

		int diff = 0, m = 0;
		String result = "";
		int pos = 0;
		for (int j = 0; j < target.length(); j++) {
			if (target.charAt(j) == ' ') {
				result += " ";
			} else {
				diff = posMap.get(target.charAt(j));
				pos = (posMap.get(key.charAt(m)) + diff) % CIPHER_CHAR_SET.length();
				result += (CIPHER_CHAR_SET.charAt(pos));
				m = (m + 1) % key.length();
			}
		}

		return result;
	}
	
	//Method to decrypt input string and return decrypted string
	public static String decrypt(String target, final String key) {
		String result = "";
		int absDiff = 0;
		int k = 0;
		for (int j = 0; j < target.length(); j++) {

			if (target.charAt(j) == ' ') {
				result += " ";
			} else {
				absDiff = (Math
						.abs((posMap.get(target.charAt(j)) - posMap.get(key.charAt(k)) + CIPHER_CHAR_SET.length())))
						% CIPHER_CHAR_SET.length();
				k = (k + 1) % key.length();
				result += CIPHER_CHAR_SET.charAt(absDiff);
			}
		}

		return result;
	}
	
	//Method to encrypt content of each file and subfolders in a directory
	public static void encryptDir(String path, final String key) {
		File dir = new File(path + ".encrypted");
		dir.mkdir();
		List<File> files = null;
		listf(path, files, dir.getName(), key);

	}
	
	//Method to through each file in subfolder for encryption
	public static void listf(String directoryName, List<File> files, String writeDirectory, String key) {
		File directory = new File(directoryName);
		FileInputStream fis = null;

		// Get all the files from a directory.
		File[] fList = directory.listFiles();
		int content;
		if (fList != null) {
			for (File file : fList) {
				if (file.isFile()) {
					files.add(file);
					// create encrypted file
					try {
						fis = new FileInputStream(file);
						while ((content = fis.read()) != -1) {														
							PrintWriter writer = new PrintWriter(writeDirectory + "/" + file.getName(), "UTF-8");
							writer.println(encrypt((Character.toString((char) content)), key));
						}

					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (fis != null)
								fis.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				} else if (file.isDirectory()) {
					
					File dir = new File(writeDirectory + "/" + file.getAbsolutePath());
					dir.mkdir();
					// Recursive call for subfolders
					listf(file.getAbsolutePath(), files, writeDirectory + "/" + file.getAbsolutePath(), key);
				}
			}
		}

	}
	
	//Method to decrypt content of each file and subfolders in a directory
	public static void decryptDir(String path, final String key) {
		File dir = new File(path + ".decrypted");
		dir.mkdir();
		List<File> files = null;
		listf(path, files, dir.getName(), key);

	}
	
	//Method to through each file in subfolder for decryption
	public static void listfDecryp(String directoryName, List<File> files, String writeDirectory, String key) {
		File directory = new File(directoryName);
		FileInputStream fis = null;

		// Get all the files from a directory.
		File[] fList = directory.listFiles();
		int content;
		if (fList != null) {
			for (File file : fList) {
				if (file.isFile()) {
					files.add(file);
					// create encrypted file
					try {
						fis = new FileInputStream(file);
						while ((content = fis.read()) != -1) {
							// convert to char and display it
							System.out.print((char) content);
							PrintWriter writer = new PrintWriter(writeDirectory + "/" + file.getName(), "UTF-8");
							writer.println(encrypt((Character.toString((char) content)), key));
						}

					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (fis != null)
								fis.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				} else if (file.isDirectory()) {
					// recursive call
					File dir = new File(writeDirectory + "/" + file.getAbsolutePath());
					dir.mkdir();
					listfDecryp(file.getAbsolutePath(), files, writeDirectory + "/" + file.getAbsolutePath(), key);
				}
			}
		}

	}

}