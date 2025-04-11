import java.util.ArrayList;
import java.util.List;

public class prog {
    String programName;
    String lengthInHex;
    Integer lengthInInt;
    String startingAddressInHex;
    Integer startingAddressInInt;  //done

    //String[] tRecordez;
    ArrayList<TRecord> tRecordez = new ArrayList<TRecord>();  //done

    //ExtDef[] defs;
    ArrayList <ExtDef> defs = new ArrayList<ExtDef>(); //done
    //String[] ExtRef;
    ArrayList<String> ExtRef = new ArrayList<String>();

    //Modification[] mod;
    ArrayList<Modification> mod = new ArrayList<Modification>();  //done

}
