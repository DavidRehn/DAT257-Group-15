package location.builder;

import javafx.scene.control.Label;

public interface LBuilder {
    public void setLabelName(String labelName );
    public void setWidth(int width);
    //public void setRow(RowMaker rowMaker);
    public void setStyl(String styl);
    public void setEvent(BBuilder bBuilder);

}
