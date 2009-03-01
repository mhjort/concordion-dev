package test.concordion;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;

import org.concordion.api.Resource;
import org.concordion.api.Target;
import org.concordion.internal.util.Check;

public class StubTarget implements Target {

    private final LinkedHashMap<Resource, String> writtenStrings = new LinkedHashMap<Resource, String>();
    
    public void copyTo(Resource resource, InputStream inputStream) throws IOException {
    }

    public void delete(Resource resource) throws IOException {
    }

    public void write(Resource resource, String s) throws IOException {
        writtenStrings.put(resource, s);
    }

    public String getWrittenString(Resource resource) {
        Check.isTrue(writtenStrings.containsKey(resource), "Expected resource '" + resource.getPath() + "' was not written to target");
        return writtenStrings.get(resource);
    }
}
