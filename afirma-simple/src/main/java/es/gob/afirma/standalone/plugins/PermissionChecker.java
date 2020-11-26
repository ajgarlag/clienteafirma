package es.gob.afirma.standalone.plugins;

/**
 * Clase para la comprobaci&oacute;n de permisos.
 */
public class PermissionChecker {

	/**
	 * Comprueba que entre los permisos de un plugin se encuentre el permisos indicado.
	 * @param info Informaci&oacute;n del plugin.
	 * @param targetPermission Permiso buscado.
	 */
	public static boolean check(final PluginInfo info, final Permission targetPermission) {
		return check(info.getPermissions(), targetPermission);
	}

	/**
	 * Comprueba que entre los permisos de un plugin se encuentre el permisos indicado.
	 * @param permissions Listado de permisos.
	 * @param targetPermission Permiso buscado.
	 */
	public static boolean check(final Permission[] permissions, final Permission targetPermission) {
		for (final Permission p : permissions) {
			if (p == targetPermission) {
				return true;
			}
		}
		return false;
	}
}
