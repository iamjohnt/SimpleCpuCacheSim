public class Cache {


    /*  Author      John Tran
    *   Date        2/20
    *
    *   About       Cache Simulator Project - Computer Architecture
    *               cache is simulated in java, using structures and objects
    *               program will take commands like a real cache,
    *               and behave like one by printing appropriate info
    *
    *   Code gist   The cache is simulated like so:
    *               The cache object has a field for cache size, block size, a memoryPointer to a Memory object
    *               The Memory object holds an array of data, simulating memory
    *               Back to the cache object...it also has an array also called cache
    *               This cache is an array of Slot objects, so cache[i] represents a Slot
    *               each slot object has a valid bit, dirty bit, tag, and a block
    *               the block is an array of shorts, so block[i] represents data, at the i'th offset
    *               the primary methods are read(address) and write(data2write, toAddress)
    *               they both take an address (write takes data2write) and extracts the tag, slot#, offset
    *               using that, they check if cache hit or miss, and act accordingly like a cache should
    *               whether it's simply reading, transfering a block, writing, etc. the results are printed*/


    // __________________________ PRIMARY FIELDS AND LOGIC ______________________________________________________________________________


    private final int CACHE_SIZE = 16;
    private final int BLOCK_SIZE = 16;
    private Memory memory;
    private Slot[] cache = new Slot[CACHE_SIZE];

    private class Slot {
        public int validBit = 0;
        public int dirtyBit = 0;
        public int tag = 0;
        public short[] block = new short[BLOCK_SIZE];
    }

    /*  Regarding   Read()
    *   Notes       takes address, isolates its parts. Using them, determines if cache hit or miss.
    *               If hit, just reads. If not, then transfers block to cache and updates valid bit to 1, then reads */
    public void read(int address) {
        int extractedTag = extractTag(address);
        int extractedSlotNum = extractSlotNum(address);
        int extractedOffset = extractOffset(address);
        Slot cacheSlot = cache[extractedSlotNum];   // this is the slot in the cache, determined by the offset we extracted

        if (isCacheHit(address)) {
            // cache hit - we just print the contents of the cache
            System.out.println("READING from Address x" + Integer.toHexString(address) + " - Cache HIT  - Reading following data that's in the cache:  ==== [ x" + Integer.toHexString(cacheSlot.block[extractedOffset]) + " ]");
        } else {
            // cache miss - we transfer the block to the cache, set the tag and valid bit, and read it
            transferBlockOf(address);
            cacheSlot.tag = extractedTag;
            cacheSlot.validBit = 1;
            System.out.println("READING from Address x" + Integer.toHexString(address) + " - Cache MISS - Brought following data from memory to cache: ==== [ x" + Integer.toHexString(cacheSlot.block[extractedOffset]) + " ] , with block, from address " + Integer.toHexString(getStartOfBlock(address)) + " - " + Integer.toHexString(getStartOfBlock(address) + 15));
        }
    }

    /*  Regarding   write()
    *   Notes       takes address, isolates its parts. Then determins if cache hit or miss
    *               If hit, writes data to offset in cache. If not, then transfers block to cache then writes to offset. Updates valid and dirty bit to 1 */
    public void write(short data2write, int address2write2) {
        int extractedTag = extractTag(address2write2);
        int extractedSlotNum = extractSlotNum(address2write2);
        int extractedOffset = extractOffset(address2write2);
        Slot cacheSlot = cache[extractedSlotNum];

        if (isCacheHit(address2write2)) {
            // cache hit
            cacheSlot.block[extractedOffset] = data2write;
            System.out.println("WRITING data [ x" + Integer.toHexString(data2write) + " ] to address x" + Integer.toHexString(address2write2) + " - Cache HIT");
        } else {
            // cache miss
            transferBlockOf(address2write2);
            cacheSlot.block[extractedOffset] = data2write;
            cacheSlot.tag = extractedTag;
            cacheSlot.dirtyBit = 1;
            cacheSlot.validBit = 1;
            System.out.println("WRITING data [ x" + Integer.toHexString(data2write) + " ] to address x" + Integer.toHexString(address2write2) + " - Cache MISS - Brought block at address " + Integer.toHexString(getStartOfBlock(address2write2)) + " - " + Integer.toHexString(getStartOfBlock(address2write2) + 15) + " to cache, and wrote data at offset");
        }
    }


    // __________________________ SECONDARY LOGIC / SETTERS / GETTERS __________________________________________________________________________________________________


    /*  Regarding   initEmptyCache
     *   Notes      inits cache full of 0's for all fields    */
    public void initEmptyCache() {
        for (int i = 0; i < cache.length; i++) {
            cache[i] = new Slot();
        }
    }

    /*  Regarding   setMemoryPointer
     *   Notes       methods in this class call methods from Memory class, so need set this pointer after constructing    */
    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    /*  Regarding   below extractTag, extractSlotNum, extractOffset
    *   Notes       takes address, extracts certain bits depending on if tag, slot, offset - by using bitwise and shifts - then returns  */
    private int extractTag(int address) {
        return (address & 0x00000F00) >>> 8;
    }

    private int extractSlotNum(int address) {
        return (address & 0x000000f0) >>> 4;
    }

    private int extractOffset(int address) {
        return (address & 0x0000000F);
    }

    /*  Regarding   isCacheHit
    *   Notes       takes and address, returns true if it's tag, matches tag in corresponding slot in cache - and if slot also has valid bit of 1   */
    private boolean isCacheHit(int address2check) {
        int extractedTag = extractTag(address2check);
        int extractedSlotNum = extractSlotNum(address2check);
        return cache[extractedSlotNum].tag == extractedTag && cache[extractedSlotNum].validBit == 1;
    }

    /*  Regarding   getStartOfBlock
    *   Notes       takes address, and determines and returns the beginning address of the first one's block*/
    private int getStartOfBlock(int address) {
        return address & 0b1111111111110000;
    }

    /*  Regarding   transferBlock
    *   Notes       takes an address, determines the destination slot number, and transfers the whole block of that address, into the slot  */
    private void transferBlockOf(int address) {
        int startOfBlock = getStartOfBlock(address);
        Slot destinationSlot = cache[extractSlotNum(address)];
        for (int i = 0; i < destinationSlot.block.length; i++) {
            destinationSlot.block[i] = memory.getMemVal(startOfBlock + i);
        }
    }

    /*  Regarding   printSlot
     *  Notes       prints a single slot's contents onto a line    */
    public void printSlot(int slotNum2print) {
        Slot curSlot = cache[slotNum2print];
        System.out.printf("%-5s %-5d %-6d", Integer.toHexString(slotNum2print), curSlot.validBit, curSlot.tag);
        for (int i = 0; i < curSlot.block.length; i++) {
            System.out.printf("%-4s", Integer.toHexString(curSlot.block[i]));
        }
        System.out.println();
    }

    /*  Regarding   displayCache
    *   Notes       prints some header info, and the loops through and prints all the slots of the cache  */
    public void printCache() {
        int cellLength = 6;
        System.out.println("Display Current Cache:");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("|        C U R R E N T   C A C H E                                               |");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("                                          Data");
        System.out.println("Slot  Valid Tag   0   1   2   3   4   5   6   7   8   9   a   b   c   d   e   f   ");
        System.out.println("_____ _____ _____ ________________________________________________________________");
        for (int slotNum = 0; slotNum < cache.length; slotNum++) {
            printSlot(slotNum);
        }
        System.out.println();
    }


    // todo create functionality to write back to memeory if dirty and cache miss
    /* when writing and cache miss - we need to check if slot is dirty. if so, we write dirty slot back to memeory, and overwite the slot with slot from memory, and write to cache slot with value we want to write
    * if not it's clean, so we can overwrite it */

    /* when reading and cache miss - we need to check if slot is dirty. if so, we write dirty slot back to memory, and overwrite the slot with slot that we just read */
}
