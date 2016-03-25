import org.cidarlab.eugene.Eugene;
import org.cidarlab.eugene.dom.Device;
import org.cidarlab.eugene.dom.NamedElement;
import org.cidarlab.eugene.dom.imp.container.EugeneArray;
import org.cidarlab.eugene.dom.imp.container.EugeneCollection;
import org.cidarlab.eugene.exception.EugeneException;
import org.cidarlab.eugene.util.DeviceUtils;

import java.io.File;


public class EugeneMajorityCircuit {

    private String home = System.getProperty("user.dir");
    private String name_Eug_file1 = home +"/src/test/resources/majority_1_default.eug";
    private String name_Eug_file2 = home +"/src/test/resources/majority_2_revorder.eug";
    private String name_Eug_file3 = home +"/src/test/resources/majority_3_NOT_NOR_groups.eug";
    private String name_Eug_file4 = home +"/src/test/resources/majority_4_subcircuits.eug";
    private String name_Eug_file5 = home +"/src/test/resources/majority_5_scrambled.eug";
    private String name_Eug_file6 = home +"/src/test/resources/majority_6_alternating.eug";


    //@Test
    public void testEugeneMajorityCircuits() {

        callEugene(name_Eug_file1);
        callEugene(name_Eug_file2);
        callEugene(name_Eug_file3);
        callEugene(name_Eug_file4);
        callEugene(name_Eug_file5);
        callEugene(name_Eug_file6);
        
    }

    public static void callEugene(String name_Eug_file) {

        try {
            Eugene e = new Eugene();

            EugeneCollection ec = e.executeFile(new File(name_Eug_file));

            EugeneArray variants = (EugeneArray) ec.get("allResults");

            for (int i = 0; i < 1; ++i) {

                NamedElement circuit = variants.getElement(i);

                if (circuit instanceof org.cidarlab.eugene.dom.Device) {

					/*
					 * The full DNA sequence of the circuit is generated by
					 * Eugene:
					 */

                    int g_index = 0;

                    for (NamedElement gate : ((Device) circuit)
                            .getComponentList()) {

                        if (gate instanceof org.cidarlab.eugene.dom.Part) {

                            NamedElement part = gate;


                        }

                        else if (gate instanceof org.cidarlab.eugene.dom.Device) {

                            String gate_name = gate.getName();

                            String g_direction = "+";

                            String o = ((Device) circuit).getOrientations(
                                    g_index).toString();

                            if (o.equals("[REVERSE]")) {
                                g_direction = "-";
                                Device reverse_gate = DeviceUtils
                                        .flipAndInvert((Device) gate);
                                gate = reverse_gate;
                            }

                            String egate = g_direction + gate_name;
                            System.out.println(egate);

                            int p_index = 0;

                            for (NamedElement part : ((Device) gate)
                                    .getComponentList()) {

                                String part_name = part.getName();

                                String p_direction = "+";

                                String op = ((Device) gate).getOrientations(
                                        p_index).toString();

                                if (op.equals("[REVERSE]")) {
                                    p_direction = "-";
                                }

                                p_index++;

                                String epart = p_direction + part_name;
                                //System.out.println("  " + epart);

                            }

                        }

                        g_index++;

                    }

                }

            }

            System.out.println("Number of Eugene solutions " + variants.getElements().size());

        } catch (EugeneException exception) {
            exception.printStackTrace();
        }

    }


}
