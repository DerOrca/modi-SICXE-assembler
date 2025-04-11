import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
//import java.io.Serial;
import java.util.*;
//import java.util.concurrent.atomic.LongAdder;

public class Main {
    public static ArrayList<prog> program = new ArrayList<prog>();
    public static Set<String> MemoryAddress= new HashSet<String>();
    public static ArrayList<String> content = new ArrayList<String>();

    public static ArrayList<String> programOrder = new ArrayList<String>();  //input from the gui  make sure the input is 6 chars
    public static int memoryStart=0;   // input from the gui

    public static ArrayList<ExtDef> allDefs = new ArrayList<ExtDef>();//3ashan access el label bl address bta3o @ modification

    public static File fileSybmol = new File("modi_sic_assembler\\untitled1\\src\\ESTabel.txt");
    public static PrintWriter ESTable;

    public static int prgcounter=0;

    public static int totalSize = 0;

    static {
        try {
            ESTable = new PrintWriter(fileSybmol);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //in case of sic/xe we need to initialize the stating address
    //this function fills the program object with the hte.. with info
    public static void load(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        //String record = scanner.nextLine();
        String record;
        //int prgcounter=0;
        int tcounter=0;
        int mcounter=0;
        int dcounter=0;

        while(scanner.hasNext()){
            record = scanner.nextLine();
            if(record.charAt(0)=='H'){
                //initailize Hs
                // ini tailize the MemoryAddress
                //1->6 name
                program.add(new prog());
               program.get(prgcounter).programName=record.substring(1,7);

               program.get(prgcounter).startingAddressInHex=record.substring(7,13);
               program.get(prgcounter).startingAddressInInt=Integer.parseInt(record.substring(7,13),16);

               program.get(prgcounter).lengthInHex=record.substring(14,19);
               program.get(prgcounter).lengthInInt=Integer.parseInt(record.substring(14,19),16);

               totalSize+=program.get(prgcounter).lengthInInt;

            }
            else if(record.charAt(0)=='D'){
                //initailize Deez nuts

                for (int i=0;i<(record.length()-1)/12;i++ )
                {
                    program.get(prgcounter).defs.add(new ExtDef());
                    program.get(prgcounter).defs.get(dcounter).lable=record.substring(1+i*12,7+i*12);
                    program.get(prgcounter).defs.get(dcounter).AddressInHex=record.substring(7+i*12,13+i*12);
                    program.get(prgcounter).defs.get(dcounter).AddressInInt=Integer.parseInt(record.substring(7+i*12,13+i*12),16);
                    //System.out.println("dcounter "+program.get(prgcounter).defs.get(dcounter).lable);
                    dcounter++;
                }

            }
            else if(record.charAt(0)=='R'){
                //initailize RRR
                for (int i=0;i<(record.length()-1)/6;i++ )
                {
                    program.get(prgcounter).ExtRef.add(record.substring( 1+(i*6),7+(i*6) ) );
                }
            }
            else if(record.charAt(0)=='T'){
                //initailize
                //m3 kol t rec gded e3ml object w 7oto fl list
                //System.out.println(Integer.parseInt("a",16));
                //System.out.println(prgcounter + "\n");

                program.get(prgcounter).tRecordez.add(new TRecord());
                program.get(prgcounter).tRecordez.get(tcounter).startInHex=record.substring(1,7);
                program.get(prgcounter).tRecordez.get(tcounter).startInInt=Integer.parseInt(record.substring(1,7),16);

                program.get(prgcounter).tRecordez.get(tcounter).lengthInHex=record.substring(7,9);
                program.get(prgcounter).tRecordez.get(tcounter).lengthInInt=Integer.parseInt(record.substring(7,9),16);

                program.get(prgcounter).tRecordez.get(tcounter).record=record.substring(9);
                tcounter++;
            }
            else if(record.charAt(0)=='M'){
                //initailize
                program.get(prgcounter).mod.add(new Modification());

                program.get(prgcounter).mod.get(mcounter).addressInHex=record.substring(1,7);
                program.get(prgcounter).mod.get(mcounter).addressInInt=Integer.parseInt(record.substring(1,7),16);

                program.get(prgcounter).mod.get(mcounter).sizeInHex=record.substring(7,9);
                program.get(prgcounter).mod.get(mcounter).sizeInInt=Integer.parseInt(record.substring(7,9),16);

                program.get(prgcounter).mod.get(mcounter).signn=record.substring(9,10);
                if(program.get(prgcounter).mod.get(mcounter).signn.equals("+")){
                    program.get(prgcounter).mod.get(mcounter).sign = true;
                    //System.out.println( program.get(prgcounter).mod.get(mcounter).sign + program.get(prgcounter).mod.get(mcounter).signn + " "+record);
                }
                else{
                    program.get(prgcounter).mod.get(mcounter).sign = false;
                    //System.out.println( program.get(prgcounter).mod.get(mcounter).sign + program.get(prgcounter).mod.get(mcounter).signn + " "+record);
                }
                program.get(prgcounter).mod.get(mcounter).lable=record.substring(10);
                mcounter++;
            }
            else if(record.charAt(0)=='E'){
                //initailize
                tcounter=0;
                mcounter=0;
                dcounter=0;
                prgcounter++;

            }
            //record = scanner.nextLine();
        }
        if(program.size() == 1){
            memoryStart = program.get(0).startingAddressInInt;
        }
        for(int i=0;i<totalSize;i+=16){
            content.add("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            MemoryAddress.add(HexAdjustment(Integer.toHexString(memoryStart+i*16),4));
        }
    }

    public static String HexAdjustment(String hex , int size){
        for(int i=hex.length() ; i<size ; i++){
            //hex.concat("0");
            hex = "0"+hex;
        }
        return hex;
    }
    /*
    public static void func2(){    // merge this function with  memAddressColumn ?!!?!?!?!?!?!?!
        String startInHex = program.get(0).startingAddressInHex;
        Integer startInInt= program.get(0).startingAddressInInt;
        Integer length = program.get(0).lengthInInt;
        Integer totalSizeInInt = length+startInInt;
        totalSizeInInt = totalSizeInInt %16;
        //ArrayList<char[]> content;
        //ArrayList<String> memoryAddress;
        /*for(int i =0; i*15 <length; i++){
            MemoryAddress.add(HexAdjustment(Integer.toHexString(i),4));
        }*/



    public static void memAddressColumn(int startOfRecordInInt , int endOfRecordInInt){
        for(int i = startOfRecordInInt; i<= endOfRecordInInt ;i=i+16){
            MemoryAddress.add(HexAdjustment(Integer.toHexString(i),4));
        }
    }


    public static Integer calcContentIdx(Integer recordStart ){//start re 4059  then filter 4050 gahza mn start of func t
        Integer offset = program.get(0).startingAddressInInt %16;//4002 then filter the 2
        Integer startFiltered = (program.get(0).startingAddressInInt - offset ) / 16;//4000 ->400
        recordStart = recordStart / 16;//4050 -> 405
        return recordStart - startFiltered; //405-400 then 5 index of rowsssssss
    }

    public static void funcT(){
        //foreach    //         for(int i =0; i < program.get(0).tRecordez.size(); i++)
        for(int pc = 0; pc<program.size();pc++){

            for(TRecord t : program.get(pc).tRecordez) {
                int offset = t.startInInt % 16;
                offset*=2;
                int startOfRecordInInt = t.startInInt- offset;   // start from 4059  to 4050  div by 10 mem addrss
                int endOffset = (t.startInInt + t.lengthInInt ) %16;
                int endOfRecordInInt = (t.startInInt + t.lengthInInt ) - endOffset;

                // fill the memoryAddress
                //loops through the record itself
                // i counts the hex   while t.len counts bytes

                Integer cLine = calcContentIdx(startOfRecordInInt);
                for(int i=0; i < t.lengthInInt*2 ;i=i+2){
                    //access el line nseeb mn zero l7d el offset (bdayet el t rec )zy ma howa w edit el gy
                    //el edit etnenat w concat ba2y el line f kol iteration l7d ma t5ls edit
                    String newCon = content.get(cLine).substring(0,offset) + t.record.substring( i , i+ 2) + content.get(cLine).substring(offset+2,32);
                    content.set(cLine,newCon);
                    offset = offset + 2;
                    if( offset >=32){
                        cLine++;
                        offset =0;
                    }
                    //content.add(1,t.record.substring( i , i+ 2).toCharArray());
                }

            }
        }
    }

    public static void ESTab(){
        int addressInc = memoryStart;
        //System.out.println(programOrder.size());
        for( int i=0 ; i< programOrder.size() ; i++){//loop 3al 7asab el given order @ array
            int currProg;
            for (currProg = 0; currProg < programOrder.size(); currProg++) {//search of index of incoming program to be worked on
                //System.out.println(programOrder.get(i) + " " +  program.get(currProg).programName );
                if (programOrder.get(i).equals(program.get(currProg).programName)) {
                    break;
                }
            }
            // file printer for the program name!!!!!!!!!!!!!!!!!!!!!!!!
            program.get(currProg).startingAddressInInt = addressInc;
            String programStartHex = HexAdjustment(Integer.toHexString(program.get(currProg).startingAddressInInt),6);

            ESTable.println(program.get(currProg).programName+"\t\t"+programStartHex);


            //for (TRecord t : program.get(currProg).tRecordez) {//incremet all addresses of all trecs
            //System.out.println(program.get(currProg).tRecordez.get(0).startInHex);
            //System.out.println(currProg);
            for(int ts = 0 ; ts<program.get(currProg).tRecordez.size();ts++){
                //System.out.println(program.get(currProg).tRecordez.get(ts).startInInt);
                //if(t.startInInt == null){
                //    break;
                //}
                program.get(currProg).tRecordez.get(ts).startInInt += addressInc;
                //System.out.println("check");
            }

            for (Modification m : program.get(currProg).mod){
                //System.out.println("mofis "+m.addressInHex);
                m.addressInInt += addressInc;
            }
            for(ExtDef ed : program.get(currProg).defs){
                ed.AddressInInt += addressInc;
                //System.out.println("defs "+ed.AddressInHex);
                allDefs.add(ed);
                //file printer for each def!!!!!!!!!!!!!!!!!!!!!!!!
                String defAddressHex = HexAdjustment(Integer.toHexString(ed.AddressInInt),6);
                ESTable.println("\t"+ed.lable+"\t"+defAddressHex);

            }
            addressInc += program.get(currProg).lengthInInt;//inc on the value by the finished program(s)
        }
    }

    public static void test() throws FileNotFoundException {
        PrintWriter test = new PrintWriter("modi_sic_assembler\\untitled1\\src\\output.txt");
        for (String str : content){
            test.println(str);
        }
        test.close();
    }

    public static void modifi(){
        for(int i =0 ; i<allDefs.size();i++){
            //System.out.println(allDefs.get(i).lable);
        }
        for(int i=0 ;i<prgcounter;i++ ){
            for(int j=0 ; j<program.get(i).mod.size();j++){
                for(int t_counter=0 ;t_counter<program.get(i).tRecordez.size();t_counter++){
                    int range = program.get(i).tRecordez.get(t_counter).startInInt + program.get(i).tRecordez.get(t_counter).lengthInInt;
                    //System.out.println(program.get(i).mod.get(j).addressInInt);
                    if(program.get(i).mod.get(j).addressInInt >= program.get(i).tRecordez.get(t_counter).startInInt && program.get(i).mod.get(j).addressInInt <range){
                        //String newCon = content.get(cLine).substring(0,offset) + t.record.substring( i , i+ 2) + content.get(cLine).substring(offset+2,32);
                        int offset = program.get(i).mod.get(j).addressInInt - program.get(i).tRecordez.get(t_counter).startInInt;
                        offset*=2;
                        String edited = null;
                        // handling the program name in modification
                        for(int pc =0 ;pc<program.size();pc++){
                            if(program.get(i).mod.get(j).lable.equals(program.get(pc).programName)){
                                int modified;
                                modified = program.get(pc).lengthInInt +  Integer.parseInt(program.get(i).tRecordez.get(t_counter).record.substring(offset,offset+6),16);
                                edited = HexAdjustment(Integer.toHexString(modified),6);
                                break;
                            }
                        }

                        // handling def label in modification
                        for(int def_counter =0 ; def_counter < allDefs.size();def_counter++){
                            if(allDefs.get(def_counter).lable.equals(program.get(i).mod.get(j).lable) ){
                                int modified;
                                if(program.get(i).mod.get(j).sign){
                                    modified = allDefs.get(def_counter).AddressInInt +  Integer.parseInt(program.get(i).tRecordez.get(t_counter).record.substring(offset,offset+6),16);
                                }
                                else{
                                    modified = allDefs.get(def_counter).AddressInInt -  Integer.parseInt(program.get(i).tRecordez.get(t_counter).record.substring(offset,offset+6),16);
                                }

                                //System.out.println( "edited before " + program.get(i).mod.get(j).sign+" " +Integer.toHexString(modified));
                                edited = HexAdjustment(Integer.toHexString(modified),6);
                                if(edited.length()>6){
                                    edited = edited.substring(1,7);
                                }
                                break;
                            }
                        }
                        program.get(i).tRecordez.get(t_counter).record = program.get(i).tRecordez.get(t_counter).record.substring(0,offset) + edited + program.get(i).tRecordez.get(t_counter).record.substring(offset+6, program.get(i).tRecordez.get(t_counter).lengthInInt*2);
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        programOrder.add("PROGAX");
        programOrder.add("PROGBX");
        memoryStart= 16384;

        //File file = new File("C:\\Users\\utd\\Downloads\\asfa\\Ta7neesh22\\untitled1\\src\\htddddeeeeREC.txt");
        File file = new File("modi_sic_assembler\\untitled1\\src\\HDRTME.txt");
        load(file); //relative lel zero
        ESTab(); // make all the prog objects shifted by its program start
        modifi();
        funcT();
        test();
        ESTable.close();
        System.out.println("Hello world!");
    }


}