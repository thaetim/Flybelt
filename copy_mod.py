"""
Copy the newly built Flybelt mod to CurseForge and remove old versions.
"""
import shutil
from pathlib import Path

MODS_DIR = Path(r"C:\Users\M\curseforge\minecraft\Instances\Gruzownia3\mods")
BUILD_DIR = Path(__file__).parent / "build" / "libs"


def _parse_version(name: str) -> tuple[int, ...]:
    """Extract version tuple from flybelt-X.Y.Z-M.N.P.Q.jar for sorting."""
    stem = name.replace(".jar", "").replace("flybelt-", "")
    parts = stem.replace("-", ".").split(".")
    return tuple(int(p) for p in parts if p.isdigit())


def main():
    # Find the newly built mod
    mod_jars = list(BUILD_DIR.glob("flybelt-*.jar"))
    if not mod_jars:
        print("No flybelt JAR found in build/libs. Run 'gradlew.bat build' first.")
        return 1

    # Filter out sources/slim, then pick the latest by version
    candidates = [
        j for j in mod_jars
        if "sources" not in j.name.lower() and "slim" not in j.name.lower()
    ]
    if not candidates:
        built_jar = mod_jars[0]  # fallback if only sources/slim exist
    else:
        built_jar = max(candidates, key=lambda j: _parse_version(j.name))

    # Remove old flybelt versions from mods folder
    MODS_DIR.mkdir(parents=True, exist_ok=True)
    removed = []
    for old in MODS_DIR.glob("flybelt*.jar"):
        old.unlink()
        removed.append(old.name)

    # Copy the new version
    dest = MODS_DIR / built_jar.name
    shutil.copy2(built_jar, dest)

    print(f"Removed: {removed or '(none)'}")
    print(f"Copied: {built_jar.name} -> {dest}")
    return 0


if __name__ == "__main__":
    exit(main())
