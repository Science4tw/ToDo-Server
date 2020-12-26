package server;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Account {

	// Emailadresse, Username und Password
	String emailAdress;
	String userName;
	String password;
	
	private String hashedPassword;
	private Instant lastLogin;

	private static Logger logger = Logger.getLogger("");

	// Liste für die Accounts
	private static final ArrayList<Account> accounts = new ArrayList<>();

	//Hash password
	private static final SecureRandom rand = new SecureRandom();
	private static final int iterations = 127;
	private final byte[] salt = new byte[64];

	
	// Konstruktor
	public Account(String username, String password) {
		this.userName = username;
		this.hashedPassword = hash(password);
		this.lastLogin = Instant.now();

	}
	
	/**
	 * Add a new account to our list of valid accounts
	 */
	public static void add(Account account) {
		synchronized (accounts) {
			accounts.add(account);
		}
	}

	/**
	 * Remove a account from our list of valid accounts
	 */
	public static void remove(Account account) {
		synchronized (accounts) {
			for (Iterator<Account> i = accounts.iterator(); i.hasNext();) {
				if (account == i.next()) i.remove();
			}
		}
	}
	
	/**
	 * Find and return an existing account
	 */
	public static Account exists(String username) {
		synchronized (accounts) {
			for (Account account : accounts) {
				if (account.userName.equals(username)) return account;
			}
		}
		return null;
	}
	
	public boolean checkPassword(String password) {
		String newHash = hash(password);
		boolean success = hashedPassword.equals(newHash);
		if (success) this.lastLogin = Instant.now();
		return success;
	}

	/**
	 * This method is here, because we have a secure random number generator already
	 * set up. We have a 32-character token - enough to be reasonably secure.
	 */
	public static String getToken() {
		byte[] token = new byte[16];
		rand.nextBytes(token);
		return bytesToHex(token);
	}
	
	// Getter & Setter

	public String getEmailAdress() {
		return emailAdress;
	}

	public void setEmailAdress(String emailAdress) {
		this.emailAdress = emailAdress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

/*
	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
	*/
	
	/**
	 * There are many sources of info on how to securely hash passwords. I'm not a
	 * crypto expert, so I follow the recommendations of the experts. Here are two
	 * examples:
	 * 
	 * https://crackstation.net/hashing-security.htm
	 * 
	 * https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
	 */
	private String hash(String password) {
		try {
			char[] chars = password.toCharArray();
			PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = skf.generateSecret(spec).getEncoded();
			return bytesToHex(hash);
		} catch (Exception e) {
			logger.severe("Secure password hashing not possible - stopping server");
			System.exit(0);
			return null; // Will never execute, but keeps Java happy
		}
	}
	// From: https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
		private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

		public static String bytesToHex(byte[] bytes) {
			char[] hexChars = new char[bytes.length * 2];
			for (int j = 0; j < bytes.length; j++) {
				int v = bytes[j] & 0xFF;
				hexChars[j * 2] = hexArray[v >>> 4];
				hexChars[j * 2 + 1] = hexArray[v & 0x0F];
			}
			return new String(hexChars);
		}

}
