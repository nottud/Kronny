package mapmodel;

import datahandler.path.PathElement;
import datahandler.path.PathNameLookup;

public abstract class BranchModel implements ParentModel, ChildModel {

	private ParentModel parent;
	protected NamedChildStorage children;

	public BranchModel(ParentModel parent) {
		this.parent = parent;
		children = new NamedChildStorage();
	}

	@Override
	public ParentModel getParentModel() {
		return parent;
	}

	public ChildModel getChildModel(PathNameLookup pathNameLookup) {
		return children.findChild(pathNameLookup);
	}

	@Override
	public PathElement createChildPathElement(ChildModel childModel) {
		return new PathNameLookup(children.getName(childModel));
	}

}
