# SimpleNick

SimpleNick is a lightweight Bukkit plugin that lets players change their display nickname on a Minecraft server. This plugin works with LuckPerms and Vault for permissions and uses LPC for color codes (e.g., `&b` for sky blue).

## Features

- `/nick <nickname>` — Change your display nickname.
- `/nick reset` — Reset your nickname to your original name.
- **Color Support** — Use color codes with LPC (e.g., `&c` for red).
- **Permissions-Based** — Control access to `/nick` by rank.

## Requirements

- Bukkit/Spigot server (1.16 or higher)
- Vault plugin
- LuckPerms plugin
- LPC plugin for color codes in nicknames

## Installation

1. Download the latest `.jar` from the [Releases](https://github.com/YoImDark/SimpleNick/releases/) section of this repository.
2. Place `SimpleNick-1.0.jar` into the `plugins` folder on your server.
3. Ensure **Vault**, **LuckPerms**, and **LPC** plugins are also installed in `plugins`.
4. Start or restart your server to load the plugin.

## Configuration

The `SimpleNick` folder is created on first load, with a `config.yml` file inside. Use `config.yml` to control which ranks can use `/nick`:

```allowed_ranks:
  - "admin"
  - "moderator"
```

This list determines which LuckPerms ranks can use `/nick` (in this exemple: `admin` and `moderator` can use the `/nick` command).

## Commands

- `/nick <nickname>` — Sets your display name.
- `/nick reset` — Resets your display name.

## Permissions
- `simplenick.use` — Allows use of `/nick`.
- Use **LuckPerms** to manage permissions based on `allowed_ranks` in `config.yml`.

## Usage

- **Change Nickname**: `/nick <nickname>`
Example: `/nick &bBlueSky` sets your nickname to `BlueSky` in sky blue.

- **Reset Nickname**: `/nick reset`
Resets your name to your original player name.

## License

This project is open-source and isn't licensed.
