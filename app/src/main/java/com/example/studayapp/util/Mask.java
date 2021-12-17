package com.example.studayapp.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/***********************************************************
 *          Mascara para EditText adaptada                 *
 *  Autor: https://pt.stackoverflow.com/users/35406/viana  *
 * *********************************************************/

public abstract class Mask {

    public static TextWatcher insert(final String mask, final EditText et) {
        return new TextWatcher() {
            boolean isUpdating;
            String oldTxt = "";
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                String str = Mask.unmask(s.toString());
                String maskCurrent = "";
                if (isUpdating) {
                    oldTxt = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > oldTxt.length()) {
                        maskCurrent += m;
                        continue;
                    }
                    try {
                        maskCurrent += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                et.setText(maskCurrent);
                et.setSelection(maskCurrent.length());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        };
    }
    public static String unmask(String string) {
        return string.replaceAll("[.]", "")
                     .replaceAll("[-]", "")
                     .replaceAll("[/]", "")
                     .replaceAll("[(]", "")
                     .replaceAll("[)]", "")
                     .replaceAll("[:]", "")
                     .replaceAll("[ ]", "");
    }

}
