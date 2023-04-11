import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*; 


public class main {
    static String[] words = new String[25143];
    static String  plaintext = "This is a top secret.";
    static String IV = "aabbccddeeff00998877665544332211";
    static byte[] IVinBytes = hexStringToByteArray(IV);
    static String ciphtertext = "764aa26b55a4da654df6b19e4bce00f4ed05e09346fb0e762583cb7da2ac93a2";
    static String decryptedCiphterText;
    static byte[]  encryptedPlaintext;
    static String keyInHex;
    static byte[] ciphtetextInBytes= hexStringToByteArray(ciphtertext);


        public static void main(String[] args) throws Exception {
            int words_index = 0;
           try {
               File myObj = new File("words.txt");
               Scanner myReader = new Scanner(myObj);
               while (myReader.hasNextLine()) {
                 String data = myReader.nextLine();
                 if(data.length() <16){
                   data= padding(data);
                   words[words_index] = data;
                   words_index++;
                 }
               }
               myReader.close();
             } catch (FileNotFoundException e) {
               System.out.println("Error reading the file.");
             } 
             for (int i = 0; i < words.length; i++) {
                solve_key(words[i],i);
             }
       }

       

        static public byte[] encryption(byte[] iv, String key) {
            try{
                IvParameterSpec IV = new IvParameterSpec(iv);
                SecretKeySpec sKey = new SecretKeySpec(key.getBytes(), "AES");
                Cipher cy = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cy.init(Cipher.ENCRYPT_MODE, sKey , IV);
                byte[] CipherText = cy.doFinal(plaintext.getBytes());
                return CipherText;
            }
            catch (Exception ex){
            }
            return null;       
            }



    private static String decryption(byte[] enc, byte[] iv, String key) {
        try{
            IvParameterSpec IV= new IvParameterSpec(iv);
            SecretKeySpec  skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE,skey,IV);
            byte[] real_plaintext = cipher.doFinal(enc);
            return new String(real_plaintext);
        }
        catch(Exception ex){
        }
        return null;
    }

    public static String padding(String a){
        String temp = a;
        if(temp.length() < 16){
            for(int i = temp.length() ; i<16 ; i++)
            temp += "#";
        }
        return temp;
    }

    public static void solve_key(String key, int wordNumber){
            try{
                 if(key !=null){
                 if( bytesToHex(encryptedPlaintext = encryption(IVinBytes , key)).toLowerCase().equals(ciphtertext)){
                decryptedCiphterText = decryption(encryptedPlaintext , IVinBytes , key);
                if (decryptedCiphterText != null){
                        System.out.println("The word number is: " + wordNumber + " when we remove the words with characters more than 16 " +"\nThe secure key is: " + key + "\nThe key in hex is: " + bytesToHex(key.getBytes()));
                    }
                    else{
                        System.out.println("no key was found");
                        return;
                        }
                      }
                    } 
                 }catch (Exception ex){
            } 
        }

        public static String bytesToHex(byte[] bytes){
            char[] HEX_array = "0123456789ABCDEF".toCharArray();
            char[] hexchars = new char[bytes.length * 2];
            for (int i = 0; i < bytes.length; i++) {
                int v = bytes[i] & 0xFF;
                hexchars[i*2] = HEX_array[v >>> 4];
                hexchars[i*2+1] = HEX_array[v & 0x0F];
            }
            return new String(hexchars);
        } 
   
    public static byte[] hexStringToByteArray(String var) {

        int len = var.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2){
            data[i / 2] = (byte) ((Character.digit(var.charAt(i), 16) << 4) + Character.digit(var.charAt(i+1), 16));
        }
        return data;
    }
  
}
