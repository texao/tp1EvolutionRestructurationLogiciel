package tp2hai913.tp2hai913;

public class CallRelation {
    private String caller;
    private String callee;

    public CallRelation(String caller, String callee) {
        this.caller = caller;
        this.callee = callee;
    }

    public String getCaller() {
        return caller;
    }

    public String getCallee() {
        return callee;
    }
}
