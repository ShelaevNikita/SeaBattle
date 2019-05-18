public enum Chip {
    KRESTIC,
    NOLIC;

    public Chip opposite() {
        if (this == KRESTIC) return NOLIC;
        else return KRESTIC;
    }
}