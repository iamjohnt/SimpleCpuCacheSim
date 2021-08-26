public class Memory {

    private short[] mm = new short[2048];

    /* initializes memory by iterating through it, and setting the value equal to the index, unless over 256
    * after 256, the value is set back to 0. This artificial constraint makes it easy to determine the index from the value*/
    public void initialize() {
        short num2store = 0;
        for (int i = 0; i < mm.length; i++) {
            mm[i] = num2store;
            num2store++;
            if (num2store == 256) {
                num2store = 0;
            }
        }
    }

    public void printMemoryInterval(int start, int end) {
        for (int i = start; i < end; i++) {
            System.out.println(mm[i]);
        }
    }

    public void printMemoryIntervalInHex(int start, int end) {
        for (int i = start; i < end; i++) {
            System.out.println(Integer.toHexString(mm[i]));
        }
    }

    // getters and setters --------------------------------------------------------------------------------

    public short getMemVal(int memLoc) {
        return mm[memLoc];
    }

    public void setMemVal(int memLoc, short val) {
        mm[memLoc] = val;
    }

}
