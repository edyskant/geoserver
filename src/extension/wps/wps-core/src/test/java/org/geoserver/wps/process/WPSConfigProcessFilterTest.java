package org.geoserver.wps.process;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.config.GeoServer;
import org.geoserver.wps.ProcessGroupInfo;
import org.geoserver.wps.ProcessGroupInfoImpl;
import org.geoserver.wps.WPSInfo;
import org.geotools.feature.NameImpl;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.process.feature.gs.FeatureGSProcessFactory;
import org.junit.Before;
import org.opengis.feature.type.Name;

/**
 * Same as {@link ProcessFilterTest} but using the WPS configuration this time
 * @author aaime
 *
 */
public class WPSConfigProcessFilterTest extends AbstractProcessFilterTest {
    
    @Before
    public void setUpInternal() throws Exception {
        
        GeoServer gs = getGeoServer();
        WPSInfo wps = gs.getService(WPSInfo.class);
        
        // remove all jts processes but buffer
        NameImpl bufferName = new NameImpl("JTS", "buffer");
        ProcessFactory jts = Processors.createProcessFactory(bufferName);
        ProcessGroupInfo jtsGroup = new ProcessGroupInfoImpl();
        jtsGroup.setFactoryClass(jts.getClass());
        jtsGroup.setEnabled(true);
        List<Name> jtsNames = new ArrayList<Name>(jts.getNames());
        jtsNames.remove(bufferName);
        jtsGroup.getFilteredProcesses().addAll(jtsNames);
        List<ProcessGroupInfo> pgs = wps.getProcessGroups();
        pgs.clear();
        pgs.add(jtsGroup);
        
        // remove the feature gs factory
        ProcessGroupInfo gsGroup = new ProcessGroupInfoImpl();
        gsGroup.setFactoryClass(FeatureGSProcessFactory.class);
        gsGroup.setEnabled(false);
        pgs.add(gsGroup);
        
        gs.save(wps);
    }
    
}
