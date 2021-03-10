package jx.fs;

public class EXT2Attribute implements FSAttribute {

    private final int id;
    private final EXT2Permission perm;

    public EXT2Attribute(int user,EXT2Permission perm) {
	this.id = user;
	this.perm = perm;
    }

    @Override
    public int getUserID() {
	return id;
    }

    @Override
    public Permission getPermission() {
	return perm;
    }
}
