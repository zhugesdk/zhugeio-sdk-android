package com.zhuge.analysis.viewSpider;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiaokang on 15/9/2.
 */
/* package */ class ViewSnapConfig {

    static JSONObject getViewSnapConfig() throws JSONException {
        String json = "{" +
                "\"config\": {\n" +
                "      \"classes\": [\n" +
                "        {\n" +
                "          \"name\": \"android.view.View\",\n" +
                "          \"properties\": [\n" +
                "            {\n" +
                "              \"name\": \"importantForAccessibility\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"isImportantForAccessibility\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Boolean\"\n" +
                "                }\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"clickable\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"isClickable\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Boolean\"\n" +
                "                }\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"alpha\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"getAlpha\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Float\"\n" +
                "                }\n" +
                "              },\n" +
                "              \"set\": {\n" +
                "                \"selector\": \"setAlpha\",\n" +
                "                \"parameters\": [\n" +
                "                  {\n" +
                "                    \"type\": \"java.lang.Float\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"hidden\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"getVisibility\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Integer\"\n" +
                "                }\n" +
                "              },\n" +
                "              \"set\": {\n" +
                "                \"selector\": \"setVisibility\",\n" +
                "                \"parameters\": [\n" +
                "                  {\n" +
                "                    \"type\": \"java.lang.Integer\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"background\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"getBackground\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"android.graphics.drawable.Drawable\"\n" +
                "                }\n" +
                "              },\n" +
                "              \"set\": {\n" +
                "                \"selector\": \"setBackground\",\n" +
                "                \"parameters\": [\n" +
                "                  {\n" +
                "                    \"type\": \"android.graphics.drawable.ColorDrawable\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"android.widget.TextView\",\n" +
                "          \"properties\": [\n" +
                "            {\n" +
                "              \"name\": \"importantForAccessibility\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"isImportantForAccessibility\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Boolean\"\n" +
                "                }\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"clickable\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"isClickable\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Boolean\"\n" +
                "                }\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"alpha\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"getAlpha\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Float\"\n" +
                "                }\n" +
                "              },\n" +
                "              \"set\": {\n" +
                "                \"selector\": \"setAlpha\",\n" +
                "                \"parameters\": [\n" +
                "                  {\n" +
                "                    \"type\": \"java.lang.Float\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"hidden\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"getVisibility\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Integer\"\n" +
                "                }\n" +
                "              },\n" +
                "              \"set\": {\n" +
                "                \"selector\": \"setVisibility\",\n" +
                "                \"parameters\": [\n" +
                "                  {\n" +
                "                    \"type\": \"java.lang.Integer\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"text\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"getText\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.CharSequence\"\n" +
                "                }\n" +
                "              },\n" +
                "              \"set\": {\n" +
                "                \"selector\": \"setText\",\n" +
                "                \"parameters\": [\n" +
                "                  {\n" +
                "                    \"type\": \"java.lang.CharSequence\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"textColor\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"getTextColors\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"android.content.res.ColorStateList\"\n" +
                "                }\n" +
                "              },\n" +
                "              \"set\": {\n" +
                "                \"selector\": \"setTextColor\",\n" +
                "                \"parameters\": [\n" +
                "                  {\n" +
                "                    \"type\": \"java.lang.Integer\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"fontSize\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"getTextSize\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Float\"\n" +
                "                }\n" +
                "              },\n" +
                "              \"set\": {\n" +
                "                \"selector\": \"setTextSize\",\n" +
                "                \"parameters\": [\n" +
                "                  {\n" +
                "                    \"type\": \"java.lang.Integer\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"type\": \"java.lang.Float\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"android.widget.ImageView\",\n" +
                "          \"properties\": [\n" +
                "            {\n" +
                "              \"name\": \"importantForAccessibility\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"isImportantForAccessibility\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Boolean\"\n" +
                "                }\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"clickable\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"isClickable\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Boolean\"\n" +
                "                }\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"alpha\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"getAlpha\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Float\"\n" +
                "                }\n" +
                "              },\n" +
                "              \"set\": {\n" +
                "                \"selector\": \"setAlpha\",\n" +
                "                \"parameters\": [\n" +
                "                  {\n" +
                "                    \"type\": \"java.lang.Float\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"hidden\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"getVisibility\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"java.lang.Integer\"\n" +
                "                }\n" +
                "              },\n" +
                "              \"set\": {\n" +
                "                \"selector\": \"setVisibility\",\n" +
                "                \"parameters\": [\n" +
                "                  {\n" +
                "                    \"type\": \"java.lang.Integer\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"image\",\n" +
                "              \"get\": {\n" +
                "                \"selector\": \"getDrawable\",\n" +
                "                \"parameters\": [],\n" +
                "                \"result\": {\n" +
                "                  \"type\": \"android.graphics.drawable.Drawable\"\n" +
                "                }\n" +
                "              },\n" +
                "              \"set\": {\n" +
                "                \"selector\": \"setImageDrawable\",\n" +
                "                \"parameters\": [\n" +
                "                  {\n" +
                "                    \"type\": \"android.graphics.drawable.BitmapDrawable\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }" +
                "}";
        return new JSONObject(json);
    }
}
