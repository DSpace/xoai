package org.dspace.xoai.dataprovider.handlers.helpers;

import org.dspace.xoai.dataprovider.exceptions.CannotDisseminateFormatException;
import org.dspace.xoai.dataprovider.model.Context;
import org.dspace.xoai.dataprovider.model.MetadataFormat;

/**
 * Helper class used to centralize preconditions for the different handlers.
 */
public class PreconditionHelper {

  /**
   * Checks that the provided metadataPrefix can be retrieved from the given {@link Context}.
   * @param context expected to be not null
   * @param metadataPrefix
   * @throws CannotDisseminateFormatException
   */
  public static void checkMetadataFormat(Context context, String metadataPrefix)
          throws CannotDisseminateFormatException {
    MetadataFormat format = context.formatForPrefix(metadataPrefix);
    if (format == null) {
        throw new CannotDisseminateFormatException("Format " + metadataPrefix + " unknown");
    }
  }
}
