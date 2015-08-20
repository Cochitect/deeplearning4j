package org.deeplearning4j.nn.conf.layers;

import lombok.*;

/**
 * Created by jeffreytang on 7/21/15.
 */
@Data @NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class FeedForwardLayer extends Layer {
    protected int nIn;
    protected int nOut;
    
    public FeedForwardLayer( Builder builder ){
    	super(builder);
    	this.nIn = builder.nIn;
    	this.nOut = builder.nOut;
    }

    public abstract static class Builder extends Layer.Builder {
        protected int nIn = Integer.MIN_VALUE;
        protected int nOut = Integer.MIN_VALUE;

        public Builder nIn(int nIn) {
            this.nIn = nIn;
            return this;
        }

        public Builder nOut(int nOut) {
            this.nOut = nOut;
            return this;
        }
    }
}
