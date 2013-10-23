package com.lyncode.xoai.testsupport.builders.dataprovider;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.BundleReference;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Referable;
import com.lyncode.xoai.testsupport.builders.ListBuilder;

import java.util.List;

public class XOAIDataProviderBundleReferenceBuilder {
    public static List<BundleReference> build(String... ids) {
        return new ListBuilder<String>().add(ids).build(new ListBuilder.Transformer<String, BundleReference>() {
            @Override
            public BundleReference transform(String elem) {
                return build(elem);
            }
        });
    }

    public static List<BundleReference> build(List<String> ids) {
        return new ListBuilder<String>().add(ids).build(new ListBuilder.Transformer<String, BundleReference>() {
            @Override
            public BundleReference transform(String elem) {
                return build(elem);
            }
        });
    }

    public static List<BundleReference> build(Referable... items) {
        return new ListBuilder<Referable>().add(items).build(new ListBuilder.Transformer<Referable, BundleReference>() {
            @Override
            public BundleReference transform(Referable elem) {
                return build(elem);
            }
        });
    }


    public static BundleReference build(String id) {
        BundleReference ref = new BundleReference();
        ref.setRefid(id);
        return ref;
    }

    public static BundleReference build(Object item) {
        BundleReference ref = new BundleReference();
        if (item instanceof Referable) ref.setRefid(((Referable) item).getId());
        else ref.setRefid(item.toString());
        return ref;
    }
}
