package es.gob.afirma.signers.batch.client;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.gob.afirma.core.misc.Base64;
import es.gob.afirma.core.misc.protocol.ParameterException;
import es.gob.afirma.core.misc.protocol.UrlParametersToSign;

/**
 * Clase encargada de parsear datos de tipo JSON para la firma de lotes monof&aacute;sica.
 * @author Jose.Montero
 */
public class LocalDataParser {

	static final String DEFAULT_URL_ENCODING = StandardCharsets.UTF_8.name();

	static final String ELEM_STOPONERROR = "stoponerror"; //$NON-NLS-1$
	static final String ELEM_SINGLESIGNS = "singlesigns"; //$NON-NLS-1$
	static final String ELEM_ID = "id"; //$NON-NLS-1$
	static final String ELEM_OP = "op"; //$NON-NLS-1$
	static final String ELEM_SUBOPERATION = "suboperation"; //$NON-NLS-1$
	static final String ELEM_DATAREFERENCE = "datareference"; //$NON-NLS-1$
	static final String ELEM_FORMAT = "format"; //$NON-NLS-1$
	static final String ELEM_ALGORITHM = "algorithm"; //$NON-NLS-1$
	static final String ELEM_PROPERTIES = "properties"; //$NON-NLS-1$
	static final String ELEM_EXTRAPARAMS = "extraParams"; //$NON-NLS-1$
	static final String ELEM_SINGLESIGNS_EXTRAPARAMS = "extraparams"; //$NON-NLS-1$

	/**
	 * Transforma un objeto json en datos procesados para llamar a la firma monof&aacute;sica por lotes.
	 * @param json datos a transformar.
	 * @return datos transformados y estructurados en datos para firma de lotes monof&aacute;sica.
	 * @throws ParameterException error al indicar alguno de los par&aacute;metros
	 * @throws IOException error de entrada o salida al decodificar a base 64
	 * @throws JSONException error al parsear JSON
	 */
	public static List<UrlParametersToSign> parseJSONToUrlParamsToSign(final JSONObject json) throws ParameterException, JSONException, IOException {

		final List<UrlParametersToSign> result = new ArrayList<UrlParametersToSign>();

		final JSONArray singleSignsArray = json.getJSONArray(ELEM_SINGLESIGNS);
		if (singleSignsArray != null) {
			for (int i = 0 ; i < singleSignsArray.length() ; i++) {
				final UrlParametersToSign urlParams = new UrlParametersToSign();
				final Map<String, String> params = new HashMap<String, String>();
				params.put(ELEM_STOPONERROR, json.has(ELEM_STOPONERROR) ? Boolean.toString(json.getBoolean(ELEM_STOPONERROR)) : "false"); //$NON-NLS-1$
				params.put(ELEM_ID, singleSignsArray.getJSONObject(i).getString(ELEM_ID));
				urlParams.setData(Base64.decode(singleSignsArray.getJSONObject(i).getString(ELEM_DATAREFERENCE)));
				params.put(ELEM_OP, singleSignsArray.getJSONObject(i).has(ELEM_SUBOPERATION) ?
									singleSignsArray.getJSONObject(i).getString(ELEM_SUBOPERATION)
									: json.getString(ELEM_SUBOPERATION)
				);
				params.put(ELEM_FORMAT, singleSignsArray.getJSONObject(i).has(ELEM_FORMAT) ?
						singleSignsArray.getJSONObject(i).getString(ELEM_FORMAT)
						: json.getString(ELEM_FORMAT)
				);
				params.put(ELEM_ALGORITHM, json.getString(ELEM_ALGORITHM));
				params.put(ELEM_PROPERTIES, singleSignsArray.getJSONObject(i).has(ELEM_SINGLESIGNS_EXTRAPARAMS) ?
						singleSignsArray.getJSONObject(i).getString(ELEM_SINGLESIGNS_EXTRAPARAMS)
						: json.has(ELEM_EXTRAPARAMS) ? json.getString(ELEM_EXTRAPARAMS) : null
						);
				urlParams.setSignParameters(params);
				result.add(urlParams);
			}
		}
		return result;
	}

}
