package com.ilzekruger.demo.batchdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class LineItemProcessor implements ItemProcessor<String, String> {

    private static final Logger log = LoggerFactory.getLogger(LineItemProcessor.class);
    private static final int SHIFT = 3; 
    
    @Override
    public String process(String item) throws Exception {

    	//Do some stuff to item and save in returnItem
    	final String returnItem = basicCaesarCipher(item);

        log.info("Converting (" + item + ") into (" + returnItem + ")");

        return returnItem;
    }

	/* 
	 * Very Basic 3 digit Caesar Cipher conversion of a string.
	 * It will only convert the Aplhabetic characters
	 *
	 */
	public static String basicCaesarCipher (String line) {
	      
        StringBuilder encrypted = new StringBuilder(line);
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String alphabet2 = alphabet.toLowerCase();
        String keyedalphabet = alphabet.substring(SHIFT) + alphabet.substring(0, SHIFT);
        for (int q = 0; q < encrypted.length(); q++) {
            char currChar = encrypted.charAt(q);
            int index = alphabet.indexOf(currChar);
            if (index != -1) {
                char newChar = keyedalphabet.charAt(index);
                encrypted.setCharAt(q, newChar);
            }
            index = alphabet2.indexOf(currChar);
            if (index != -1) {
                String keyedalphabet2 = keyedalphabet.toLowerCase();
                char newChar = keyedalphabet2.charAt(index);
                encrypted.setCharAt(q, newChar);
            }
        }
        return encrypted.toString();
    }
}
