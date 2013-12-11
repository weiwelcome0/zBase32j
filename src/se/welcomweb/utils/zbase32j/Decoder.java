package se.welcomweb.utils.zbase32j;

public class Decoder extends ZBase32 {
    public String decode(String input) {
        int expOrgSize = (int)Math.floor(input.length() / 1.6);
        int expPadSize = ((int)Math.ceil(expOrgSize / 5.0)) * 8;
        StringBuilder s= new StringBuilder(input);
        for (int i = 0; i < expPadSize; i++) {
            s.append("=");
        }
        
        char[] data    = s.toString().toLowerCase().toCharArray();
        int dataLen    = data.length;
        while (dataLen > 0) {
            if (!this.ignore(data[dataLen - 1]))
                break;
            
            dataLen--;
        }
        
        java.util.List<Byte> output = new java.util.ArrayList<Byte>();
        int i = 0;
        int e = dataLen - 8;
        for (i = this.next(data, i, e); i < e; i = this.next(data, i, e)) {
            byte b1 = decoding[data[i++]];
            i = this.next(data, i, e);
            byte b2 = decoding[data[i++]];
            i = this.next(data, i, e);
            byte b3 = decoding[data[i++]];
            i = this.next(data, i, e);
            byte b4 = decoding[data[i++]];
            i = this.next(data, i, e);
            byte b5 = decoding[data[i++]];
            i = this.next(data, i, e);
            byte b6 = decoding[data[i++]];
            i = this.next(data, i, e);
            byte b7 = decoding[data[i++]];
            i = this.next(data, i, e);
            byte b8 = decoding[data[i++]];
            
            output.add((byte) ((b1 << 3) | (b2 >> 2)));
            output.add((byte) ((b2 << 6) | (b3 << 1) | (b4 >> 4)));
            output.add((byte) ((b4 << 4) | (b5 >> 1)));
            output.add((byte) ((b5 << 7) | (b6 << 2) | (b7 >> 3)));
            output.add((byte) ((b7 << 5) | b8));
        }
        
        if (data[dataLen - 6] == '=') {
            output.add((byte) (((decoding[data[dataLen - 8]]) << 3) | (decoding[data[dataLen - 7]] >> 2)));
        } else if(data[dataLen - 4] == '=') {
            output.add((byte) (((decoding[data[dataLen - 8]]) << 3) | (decoding[data[dataLen - 7]] >> 2)));
            output.add((byte) (((decoding[data[dataLen - 7]]) << 6) | (decoding[data[dataLen - 6]] << 1) | (decoding[data[dataLen - 5]] >> 4)));
        } else if(data[dataLen - 3] == '=') {
            output.add((byte) (((decoding[data[dataLen - 8]]) << 3) | (decoding[data[dataLen - 7]] >> 2)));
            output.add((byte) (((decoding[data[dataLen - 7]]) << 6) | (decoding[data[dataLen - 6]] << 1) | (decoding[data[dataLen - 5]] >> 4)));
            output.add((byte) (((decoding[data[dataLen - 5]]) << 4) | (decoding[data[dataLen - 4]] >> 1)));
        } else if(data[dataLen - 1] == '=') {
            output.add((byte) (((decoding[data[dataLen - 8]]) << 3) | (decoding[data[dataLen - 7]] >> 2)));
            output.add((byte) (((decoding[data[dataLen - 7]]) << 6) | (decoding[data[dataLen - 6]] << 1) | (decoding[data[dataLen - 5]] >> 4)));
            output.add((byte) (((decoding[data[dataLen - 5]]) << 4) | (decoding[data[dataLen - 4]] >> 1)));
            output.add((byte) (((decoding[data[dataLen - 4]]) << 7) | (decoding[data[dataLen - 3]] << 2) | (decoding[data[dataLen - 2]] >> 3)));
        } else {
            output.add((byte) (((decoding[data[dataLen - 8]]) << 3) | (decoding[data[dataLen - 7]] >> 2)));
            output.add((byte) (((decoding[data[dataLen - 7]]) << 6) | (decoding[data[dataLen - 6]] << 1) | (decoding[data[dataLen - 5]] >> 4)));
            output.add((byte) (((decoding[data[dataLen - 5]]) << 4) | (decoding[data[dataLen - 4]] >> 1)));
            output.add((byte) (((decoding[data[dataLen - 4]]) << 7) | (decoding[data[dataLen - 3]] << 2) | (decoding[data[dataLen - 2]] >> 3)));
            output.add((byte) (((decoding[data[dataLen - 2]]) << 5) | (decoding[data[dataLen - 1]])));
        }
        
        byte[] b = this.toPrimitive(output.toArray(new Byte[0]));
        return this.trim(new String(b));
    }
    
    private String trim(String s) {
        char[] c = s.toCharArray();
        int end  = c.length;
        
        for (int i = c.length - 1; i >= 0; i--) {
            if (((int)c[i]) != 0)
                break;
            
            end = i;
        }
        
        return s.substring(0, end);
    }
    
    private int next(char[] data, int i, int e) {
        while ((i < e) && this.ignore(data[i]))
            i++;
        
        return i;
    }
    
    private boolean ignore(char c) {
        return (c == '\n') || (c == '\r') || (c == '\t') || (c == ' ') || (c == '-');
    }
    
    private byte[] toPrimitive(Byte[] bytes) {
        byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = bytes[i];
        }
        
        return result;
    }
}
