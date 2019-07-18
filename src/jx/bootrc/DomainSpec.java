package jx.bootrc;

public class DomainSpec extends Spec {
    ComponentSpec[] comp;
    void setComponents(ComponentSpec[] c) {
	comp = c;
    }
    public ComponentSpec[] getComponents() {
	return comp;
    }
}
