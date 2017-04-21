abstract class javaprs extends javadcl implements javadef
{
    public final static int original_state(int state) { return -check(state); }
    public final static int asi(int state) { return asb[original_state(state)]; }
    static int nasi(int state) { return nasb[original_state(state)]; }

    public final static int nt_action(int state, int sym)
    {
        return action[state + sym];
    }

    public final static int t_action(int state, int sym, LexStream stream)
    {
        return action[check(state + sym) == sym ? state + sym : state];
    }
}
