package info.devras.helper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

public class CommandHelper {
	public static void addNewCommand(String name, Plugin plugin, CommandExecutor executor, CommandOption option) {
		try {
			Constructor<?> cs = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			cs.setAccessible(true);

			PluginCommand cmd = (PluginCommand) cs.newInstance(name, plugin);
			cmd.setUsage(option.getUsage());
			cmd.setDescription(option.getDescription());
			cmd.setPermission(option.getPermission());
			cmd.setPermissionMessage(option.getPermissionMessage());
			if (option.getTabCompleter() != null) {
				cmd.setTabCompleter(option.getTabCompleter());
			}

			cmd.setAliases(option.getAliases());

			cmd.setExecutor(executor);

			Object result = getPrivateField(Bukkit.getServer().getPluginManager(), "commandMap");
			SimpleCommandMap cmdMap = (SimpleCommandMap) result;
			cmdMap.register(plugin.getName(), cmd);


			System.out.println("[CommandHelper] Register: " + plugin.getName() + ":" + cmd.getName());

			/*
			Class<?> CraftServerClass = Class.forName("org.bukkit.craftbukkit." + SrvUtil.getServerVersion() + ".CraftServer");
			Object server = CraftServerClass.cast(Bukkit.getServer());

			Object cmdMap = server.getClass().getMethod("getCommandMap", new Class[0]).invoke(server, new Object[0]);

			cmdMap.getClass().getMethod("register", new Class[] {
					String.class,
					cmd.getClass()
			}).invoke(cmdMap, new Object[] {
					plugin.getName().toLowerCase(),
					cmd
			});
			*/

			/*
			CraftServer server1 = (CraftServer) Bukkit.getServer();

			SimpleCommandMap map = server1.getCommandMap();
			map.register("minecraft", cmd);
			*/

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Object getPrivateField(Object object, String field) throws Exception {
		Class<?> clazz = object.getClass();
		Field objField = clazz.getDeclaredField(field);
		objField.setAccessible(true);
		Object res = objField.get(object);
		objField.setAccessible(false);
		return res;
	}

	public static class CommandOption{
		public CommandOption() {
			this.usage = "";
			this.description = "";
			this.permission = "";
			this.permissionMessage = "You don't have permission";
			this.tabCompleter = null;
			this.aliases = new ArrayList<>();
		}

		private String usage;
		private String description;
		private String permission;
		private String permissionMessage;
		private TabCompleter tabCompleter;
		private List<String> aliases;

		public String getUsage() {
			return usage;
		}
		public void setUsage(String usage) {
			this.usage = usage;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getPermission() {
			return permission;
		}
		public void setPermission(String permission) {
			this.permission = permission;
		}
		public String getPermissionMessage() {
			return permissionMessage;
		}
		public void setPermissionMessage(String permissionMessage) {
			this.permissionMessage = permissionMessage;
		}
		public TabCompleter getTabCompleter() {
			return tabCompleter;
		}
		public void setTabCompleter(TabCompleter tabCompleter) {
			this.tabCompleter = tabCompleter;
		}

		public List<String> getAliases(){
			return aliases;
		}
		public void setAliases(List<String> aliases) {
			this.aliases = aliases;
		}
	}
}
